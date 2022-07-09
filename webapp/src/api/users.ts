import { CreateUserData } from "../types";
import api from "./api";

export async function getMe() {
  const { data } = await api.get("/users/me");
  return data;
}

export async function createUser(form: CreateUserData) {
  const { data } = await api.post("/users", form);
  return data;
}
