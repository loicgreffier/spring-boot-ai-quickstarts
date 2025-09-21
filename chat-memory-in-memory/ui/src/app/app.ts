import { HttpClient } from '@angular/common/http';
import { Component, inject, NgZone, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Observable, Subscriber } from 'rxjs';

import { environment } from '../environments/environment';

enum Type {
	User = 'USER',
	Assistant = 'ASSISTANT'
}

interface Message {
	messageType: Type;
	text: string;
}

@Component({
	selector: 'app-root',
	imports: [FormsModule, ReactiveFormsModule],
	templateUrl: './app.html',
	styleUrl: './app.css'
})
export class App implements OnInit {
	public static readonly backendUrl = `${environment.backend_url}`;
	protected readonly Type = Type;
	private readonly zone = inject(NgZone);
	private readonly httpClient = inject(HttpClient);

	conversationId = '1';
	userInput = '';
	messages: Message[] = [];
	loading = false;

	/**
	 * Initializes the component and fetches the conversation messages.
	 */
	ngOnInit(): void {
		this.getConversationHistory();
	}

	/**
	 * Sends the user's message to the backend and handles the response via Server-Sent Events (SSE).
	 * It updates the chat history with the user's message and streams the bot's response in real-time.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		this.messages.push({ messageType: Type.User, text: this.userInput });
		this.loading = true;

		let botResponse = '';

		this.connectToServerSentEvents(
			`${App.backendUrl}/chat/${this.conversationId}?userInput=${encodeURIComponent(this.userInput)}`
		).subscribe({
			next: (event) => {
				if (botResponse.length === 0) {
					botResponse += JSON.parse(event.data).value;
					this.messages.push({ messageType: Type.Assistant, text: botResponse });
				} else {
					botResponse += JSON.parse(event.data).value;
					this.messages[this.messages.length - 1] = { messageType: Type.Assistant, text: botResponse };
				}
				this.loading = false;
			},
			error: () => {
				this.messages[this.messages.length - 1] = {
					messageType: Type.Assistant,
					text: botResponse || 'Error contacting server.'
				};
				this.loading = false;
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
	 * Fetches the conversation history from the backend and updates the messages array.
	 */
	getConversationHistory() {
		this.httpClient.get<Message[]>(`${App.backendUrl}/chat/${this.conversationId}/history`).subscribe({
			next: (messages: Message[]) => {
				if (messages.length > 0) {
					this.messages = messages;
				}
			},
			error: () => {
				this.messages = [];
			}
		});
	}
}
