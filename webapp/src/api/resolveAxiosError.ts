import { AxiosError } from "axios";
import { ErrorResponse } from "../types";

/**
 * 서버에 정의된 에러 응답이 주어지면 DefinedError 에러를 다시 던집니다.
 * @param error
 * @param message
 */
export default function resolveAxiosError(error: Error, message: string) {
  if (error instanceof AxiosError) {
    const data = error?.response?.data;

    if (data?.error) {
      throw new DefinedError(data, message);
    }
  }

  console.error(error);
}

export class DefinedError extends Error {
  data: ErrorResponse;

  constructor(data: ErrorResponse, message: string) {
    super(message);
    this.data = data;
  }
}
