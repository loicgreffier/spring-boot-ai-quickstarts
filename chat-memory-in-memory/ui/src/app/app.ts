import { HttpClient } from '@angular/common/http';
import { Component, inject, NgZone, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable, Subscriber } from 'rxjs';

import { environment } from '../environments/environment';

interface ChatMessage {
	user: string;
	bot: string;
}

interface Conversation {
	name: string;
	loading: boolean;
	messages: ChatMessage[];
}

@Component({
	selector: 'app-root',
	imports: [FormsModule],
	templateUrl: './app.html',
	styleUrl: './app.css'
})
export class App implements OnInit {
	public static readonly backendUrl = `${environment.backend_url}`;
	private readonly zone = inject(NgZone);
	private readonly httpClient = inject(HttpClient);

	userInput = '';
	conversations: Conversation[] = [];
	selectedConversation: string | null = null;

	/**
	 * Initializes the component.
	 */
	ngOnInit(): void {
		this.getConversationNames();
	}

	/**
	 * Sends the user's message to the backend and handles the response via Server-Sent Events (SSE).
	 * It updates the chat history with the user's message and streams the bot's response in real-time.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		const conversation = this.conversations.find((c) => c.name === this.selectedConversation);

		if (conversation) {
			conversation.messages.push({ user: this.userInput, bot: '' });
			conversation.loading = true;
		}

		let botResponse = '';

		this.connectToServerSentEvents(
			`${App.backendUrl}/chat/${encodeURIComponent(this.selectedConversation!)}?userInput=${encodeURIComponent(this.userInput)}`
		).subscribe({
			next: (event) => {
				if (conversation) {
					botResponse += JSON.parse(event.data).value;
					conversation.messages[conversation.messages.length - 1].bot = botResponse;
					conversation.loading = false;
				}
			},
			error: () => {
				if (conversation) {
					conversation.messages[conversation.messages.length - 1].bot = botResponse || 'Error contacting server.';
					conversation.loading = false;
				}
			}
		});

		this.userInput = '';
	}

	/**
	 * Establishes a connection to the server using Server-Sent Events (SSE).
	 *
	 * @param url The URL to connect to for receiving SSE messages.
	 */
	connectToServerSentEvents(url: string): Observable<MessageEvent> {
		return new Observable((subscriber: Subscriber<MessageEvent>) => {
			const eventSource = new EventSource(url);

			eventSource.onmessage = (event: MessageEvent) => {
				this.zone.run(() => subscriber.next(event));
			};

			eventSource.onerror = (error) => {
				this.zone.run(() => {
					subscriber.error(error);
					eventSource.close();
				});
			};
		});
	}

	/**
	 * Gets the chat history of the selected conversation.
	 */
	get chatHistory() {
		return this.conversations.find((c) => c.name === this.selectedConversation)?.messages || [];
	}

	/**
	 * Indicates whether the selected conversation is currently loading a response.
	 */
	get loading(): boolean {
		return this.conversations.find((c) => c.name === this.selectedConversation)?.loading || false;
	}

	/**
	 * Creates a new conversation with a unique name.
	 */
	createConversation() {
		const name = prompt('Enter a unique name for the new conversation:');
		if (name && !this.conversations.find((c) => c.name === name)) {
			this.conversations.push({ name, loading: false, messages: [] });
			this.selectedConversation = name;
		} else if (this.conversations.find((c) => c.name === name)) {
			alert('A conversation with that name already exists.');
		}
	}

	/**
	 * Fetches the list of conversation names from the backend and initializes the conversations array.
	 */
	getConversationNames() {
		return this.httpClient
			.get<string[]>(`${App.backendUrl}/chat/conversations`)
			.subscribe((conversationNames: string[]) => {
				this.conversations = conversationNames.map((name) => ({
					name,
					loading: false,
					messages: []
				}));

				if (this.conversations.length && !this.selectedConversation) {
					this.selectedConversation = this.conversations[0].name;
				}
			});
	}

	/**
	 * Deletes the conversation with the specified name.
	 *
	 * @param name The name of the conversation to delete.
	 */
	deleteConversation(name: string) {
		const index = this.conversations.findIndex((c) => c.name === name);
		if (index !== -1) {
			this.httpClient
				.delete(`${App.backendUrl}/chat/${encodeURIComponent(this.selectedConversation!)}`)
				.subscribe(() => {
					this.conversations.splice(index, 1);
					if (this.selectedConversation === name) {
						this.selectedConversation = this.conversations.length ? this.conversations[0].name : null;
					}
				});
		}
	}

	/**
	 * Switches the selected conversation to the one with the specified name.
	 *
	 * @param name The name of the conversation to switch to.
	 */
	switchConversation(name: string) {
		this.selectedConversation = name;
	}
}
