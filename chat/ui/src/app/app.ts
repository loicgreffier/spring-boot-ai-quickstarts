import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

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
	private readonly httpClient = inject(HttpClient);

	userInput = '';
	chatHistory: ChatMessage[] = [];
	loading = false;

	/**
	 * Sends the user's message to the backend and handles the response.
	 * It updates the chat history with the user's message and the bot's response.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		this.chatHistory.push({ user: this.userInput, bot: '' });
		this.loading = true;

		this.httpClient
			.get(`${App.backendUrl}/chat?userInput=${encodeURIComponent(this.userInput)}`, { responseType: 'text' })
			.subscribe({
				next: (response) => {
					this.chatHistory[this.chatHistory.length - 1].bot = response;
					this.loading = false;
				},
				error: () => {
					this.chatHistory[this.chatHistory.length - 1].bot = 'Error contacting server.';
					this.loading = false;
				}
			});

		this.userInput = '';
	}
}
