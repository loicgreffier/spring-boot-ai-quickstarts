import { Component, inject, NgZone } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Observable, Subscriber } from 'rxjs';

import { environment } from '../environments/environment';

interface ChatMessage {
	user: string;
	bot: string;
}

@Component({
	selector: 'app-root',
	imports: [FormsModule],
	templateUrl: './app.html',
	styleUrl: './app.css'
})
export class App {
	public static readonly backendUrl = `${environment.backend_url}`;
	private readonly zone = inject(NgZone);

	userInput = '';
	chatHistory: ChatMessage[] = [];
	loading = false;

	/**
	 * Sends the user's message to the backend and handles the response via Server-Sent Events (SSE).
	 * It updates the chat history with the user's message and streams the bot's response in real-time.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		this.chatHistory.push({ user: this.userInput, bot: '' });
		this.loading = true;

		let botResponse = '';

		this.connectToServerSentEvents(`${App.backendUrl}/chat?userInput=${encodeURIComponent(this.userInput)}`).subscribe({
			next: (event) => {
				botResponse += JSON.parse(event.data).value;
				this.chatHistory[this.chatHistory.length - 1].bot = botResponse;
				this.loading = false;
			},
			error: () => {
				this.chatHistory[this.chatHistory.length - 1].bot = botResponse || 'Error contacting server.';
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
}
