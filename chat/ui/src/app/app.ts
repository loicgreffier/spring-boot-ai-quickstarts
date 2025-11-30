import { HttpClient } from '@angular/common/http';
import { afterNextRender, Component, effect, ElementRef, inject, Injector, signal, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';

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
	imports: [FormsModule],
	templateUrl: './app.html',
	styleUrl: './app.css'
})
export class App {
	public static readonly backendUrl = `${environment.backend_url}`;
	protected readonly Type = Type;
	private readonly httpClient = inject(HttpClient);
	private injector = inject(Injector);

	@ViewChild('chatContent') chatContent!: ElementRef<HTMLDivElement>;

	userInput = '';
	messages = signal<Message[]>([]);
	loading = false;

	/**
	 * Constructor.
	 * Sets up an effect to scroll to the bottom of the chat content whenever messages change.
	 */
	constructor() {
		effect(() => {
			this.messages();

			afterNextRender(
				() => {
					this.scrollToBottom();
				},
				{ injector: this.injector }
			);
		});
	}

	/**
	 * Scrolls the chat content to the bottom to ensure the latest messages are visible.
	 */
	scrollToBottom() {
		this.chatContent?.nativeElement?.scrollTo({
			top: this.chatContent.nativeElement.scrollHeight,
			behavior: 'smooth'
		});
	}

	/**
	 * Sends the user's message to the backend and handles the response.
	 * It updates the chat history with the user's message and the bot's response.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		this.messages.update((msgs) => [...msgs, { messageType: Type.User, text: this.userInput }]);
		this.loading = true;

		this.httpClient
			.get(`${App.backendUrl}/chat?userInput=${encodeURIComponent(this.userInput)}`, { responseType: 'text' })
			.subscribe({
				next: (response) => {
					this.messages.update((msgs) => [...msgs, { messageType: Type.Assistant, text: response }]);
					this.loading = false;
				},
				error: () => {
					this.messages.update((msgs) => [...msgs, { messageType: Type.Assistant, text: 'Error contacting server.' }]);
					this.loading = false;
				}
			});

		this.userInput = '';
	}

	/**
	 * Basic formatting for the assistant's text.
	 * Replace ** with bold and * with bullet points.
	 *
	 * @param text The input text to format.
	 */
	formatText(text: string): string {
		return text.replace(/\*\*(.+?)\*\*/g, '<b>$1</b>').replace(/\*/g, '<br> • ');
	}
}
