import { useQuery, useQueryClient } from "react-query";
import api from "../api/api";
import { getMe } from "../api/users";
import { UserInfo } from "../types";

function useAuth() {
  const { data: me, isLoading } = useQuery<UserInfo>("me", getMe);

  const queryClient = useQueryClient();

  const logout = async () => {
    try {
      await api.get("/logout");
      queryClient.resetQueries("me");
    } catch (e) {}
  };

  return {
    me,
    logout,
    isLoading,
  };
}

export default useAuth;
