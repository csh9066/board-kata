import { useEffect, useState } from "react";
import { useQuery, useQueryClient } from "react-query";
import api from "../api/api";
import { getMe } from "../api/users";
import { UserInfo } from "../types";

function useAuth() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const { data: me } = useQuery<UserInfo>("me", getMe);

  useEffect(() => {
    setIsLoggedIn(me ? true : false);
  }, [me]);

  const queryClient = useQueryClient();

  const logout = async () => {
    try {
      await api.get("/logout");
      queryClient.resetQueries("me");
    } catch (e) {}
  };

  return {
    isLoggedIn,
    me,
    logout,
  };
}

export default useAuth;
