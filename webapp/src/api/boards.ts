import api from "./api";
import { PageResponse } from "./common";

export async function getBoards(page: number) {
  try {
    const { data } = await api.get<PageResponse<Board>>("/boards", {
      params: { page },
    });
    return data;
  } catch (e) {
    console.log(e);
  }
}

export interface Board {
  id: number;
  content: number;
  title: number;
  author: BoardAuthor;
  createdAt: String;
  updatedAt: String;
}

export interface BoardAuthor {
  id: number;
  nickname: number;
}
