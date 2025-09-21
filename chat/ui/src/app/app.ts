import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
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

	userInput = '';
	messages: Message[] = [];
	loading = false;

	/**
	 * Sends the user's message to the backend and handles the response.
	 * It updates the chat history with the user's message and the bot's response.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		this.messages.push({ messageType: Type.User, text: this.userInput });
		this.loading = true;

		this.httpClient
			.get(`${App.backendUrl}/chat?userInput=${encodeURIComponent(this.userInput)}`, { responseType: 'text' })
			.subscribe({
				next: (response) => {
					this.messages.push({ messageType: Type.Assistant, text: response });
					this.loading = false;
				},
				error: () => {
					this.messages.push({ messageType: Type.Assistant, text: 'Error contacting server.' });
					this.loading = false;
				}
			});

		this.userInput = '';
	}
}
