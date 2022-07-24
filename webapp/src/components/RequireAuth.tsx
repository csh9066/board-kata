import { Navigate } from "react-router-dom";
import useAuth from "../hooks/useAuth";

function RequireAuth({ children }: { children: JSX.Element }) {
  const { me } = useAuth();

  if (!me) {
    return <Navigate to="/login" />;
  }

  return children;
}

export default RequireAuth;
