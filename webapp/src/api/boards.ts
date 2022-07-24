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

export async function getBoardDetail(id: number) {
  try {
    const { data } = await api.get<Board>(`/boards/${id}`);
    return data;
  } catch (e) {
    console.log(e);
  }
}

export async function createBoard(creationData: CreateBoardData) {
  const { data } = await api.post<Board>(`/boards`, creationData);
  return data;
}

export interface Board {
  id: number;
  content: string;
  title: string;
  author: BoardAuthor;
  createdAt: string;
  updatedAt: string;
}

export interface BoardAuthor {
  id: number;
  nickname: number;
}

export interface CreateBoardData {
  title: string;
  content: string;
}
