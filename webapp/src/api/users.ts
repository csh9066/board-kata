import { CreateUserData } from "../types";
import resolveAxiosError from "./resolveAxiosError";
import api from "./api";

export async function getMe() {
  try {
    const { data } = await api.get("/users/me");
    return data;
  } catch (e) {}
}

export async function createUser(form: CreateUserData) {
  try {
    const { data } = await api.post("/users", form);
    return data;
  } catch (e) {
    resolveAxiosError(e as Error, "로그인 요청 에러");
  }
}
