import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {BookRequest} from "../../../../services/models/book-request";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BookService} from "../../../../services/services/book.service";

@Component({
  selector: 'app-manage-book',
  standalone: true,
  imports: [
    NgIf,
    NgForOf,
    FormsModule,
    RouterLink
  ],
  templateUrl: './manage-book.component.html',
  styleUrl: './manage-book.component.scss'
})
export class ManageBookComponent implements OnInit {

  errorMsg: Array<string> = [];
  selectedBookCover: any;
  selectedPicture: string | undefined;
  bookRequest: BookRequest = {authorName: "", isbn: "", synopsis: "", title: ""};

  constructor(
    private bookService: BookService,
    private router: Router,
    private activatedRoute: ActivatedRoute
  ) {
  }

  /**
   * Обрабатывает событие выбора файла.
   * @param event Событие выбора файла, которое содержит информацию о выбранных файлах.
   */
  onFileSelected(event: any) {
    // Извлекает первый файл из списка выбранных файлов.
    this.selectedBookCover = event.target.files[0];

    // Выводит информацию о выбранном файле в консоль.
    console.log(this.selectedBookCover);

    // Если файл был выбран (не null и не undefined),
    if (this.selectedBookCover) {
      // Создает объект FileReader для чтения файла.
      const reader = new FileReader();

      // Обработчик события onload, который будет вызван, когда файл полностью прочитан.
      reader.onload = () => {
        // Преобразует прочитанные данные в строку в формате Data URL (Base64) и сохраняет в переменную.
        this.selectedPicture = reader.result as string;
      };

      // Начинает чтение файла в формате Data URL (обычно для отображения в виде изображения).
      reader.readAsDataURL(this.selectedBookCover);
    }
  }

  saveBook() {
    this.bookService.saveBook({
      body: this.bookRequest
    }).subscribe({
      next: (bookId) => {
        this.bookService.uploadBookCoverPicture({
          'book-id': bookId,
          body: {
            file: this.selectedBookCover
          }
        }).subscribe({
          next: () => {
            this.router.navigate(['/books/my-books']);
          }
        })
      }, error: (err) => {
        this.errorMsg = err.error.validationErrors;
      }
    })
  }

  ngOnInit(): void {
    // Получает значение параметра `bookId` из URL (параметры пути)
    const bookId = this.activatedRoute.snapshot.params['bookId'];

    // Если `bookId` не `null` или не `undefined`, продолжает выполнение
    if (bookId) {
      // Запрос к `bookService`, чтобы найти книгу по идентификатору
      this.bookService.findBookById({"book-id": bookId}).subscribe({
        // Обработчик успешного ответа (`next`)
        next: (book) => {
          // Создание объекта `bookRequest` на основе полученных данных о книге
          this.bookRequest = {
            id: book.id, // Идентификатор книги
            title: book.title as string, // Название книги
            authorName: book.authorName as string, // Имя автора
            isbn: book.isbn as string, // ISBN книги
            synopsis: book.synopsis as string, // Краткое описание книги
            shareable: book.shareable // Признак, можно ли делиться книгой
          };
          if(book.cover){
            this.selectedPicture = 'data: image/jpg;base64, ' + book.cover;
          }
        },
        // Обработчик ошибок (`error`)
        error: (error) => {
          // Логирование ошибки и дополнительная обработка
          console.error("Error fetching book by ID:", error);
          // Можно добавить уведомление пользователя или выполнить другие действия
        },
        // Обработчик завершения (`complete`)
        complete: () => {
          // Этот блок выполняется, когда подписка завершается успешно
          console.log("Completed fetching book details.");
          // Можно добавить дополнительные действия, если требуется
        }
      });
    }
  }
}
