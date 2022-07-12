import resolveAxiosError from "./resolveAxiosError";
import api from "./api";

export async function login(email: string, password: string) {
  try {
    const { data } = await api.post("/login", { email, password });
    return data;
  } catch (e) {
    resolveAxiosError(e as Error, "로그인 요청 에러");
  }
}
