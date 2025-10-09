import {
	afterNextRender,
	Component,
	effect,
	ElementRef,
	inject,
	Injector,
	NgZone,
	signal,
	ViewChild
} from '@angular/core';
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
export class App {
	public static readonly backendUrl = `${environment.backend_url}`;
	protected readonly Type = Type;
	private readonly zone = inject(NgZone);
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
	 * Sends the user's message to the backend and handles the response via Server-Sent Events (SSE).
	 * It updates the chat history with the user's message and streams the bot's response in real-time.
	 */
	sendMessage() {
		if (!this.userInput.trim()) return;

		this.messages.update((msgs) => [...msgs, { messageType: Type.User, text: this.userInput }]);
		this.loading = true;

		let assistantMessageIndex: number;
		const url = `${App.backendUrl}/chat?userInput=${encodeURIComponent(this.userInput)}`;
		this.streamServerEvents(url).subscribe({
			next: (event) => {
				if (assistantMessageIndex) {
					this.messages.update((msgs) => {
						const updated = [...msgs];
						updated[assistantMessageIndex].text += JSON.parse(event.data).value;
						return updated;
					});
				} else {
					this.messages.update((msgs) => [
						...msgs,
						{ messageType: Type.Assistant, text: JSON.parse(event.data).value }
					]);
					assistantMessageIndex = this.messages().length - 1;
				}

				this.loading = false;
			},
			error: () => {
				if (!assistantMessageIndex) {
					this.messages.update((msgs) => [...msgs, { messageType: Type.Assistant, text: 'Error contacting server.' }]);
				}

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
	streamServerEvents(url: string): Observable<MessageEvent> {
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
