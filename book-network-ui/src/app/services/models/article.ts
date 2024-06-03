export interface Article {
  id: number;
  title: string;
  content: string;
  themeId: number; // Идентификатор темы, к которой относится статья
}
