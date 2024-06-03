import {Article} from "./article";

export interface Theme {
  id: number;
  name: string;
  articles: Article[]; // Если вы хотите хранить список статей в каждой теме
}
