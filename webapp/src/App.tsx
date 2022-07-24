import "antd/dist/antd.min.css";
import "./index.css";
import "react-quill/dist/quill.snow.css";
import "moment/locale/ko";
import { Route, Routes } from "react-router-dom";
import useAuth from "./hooks/useAuth";
import RequireAuth from "./components/RequireAuth";
import BoardDetailPage from "./pages/BoardDetailPage";
import BoardWritePage from "./pages/BoardWritePage";
import IndexPage from "./pages/IndexPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";

function App() {
  const { isLoading } = useAuth();

  if (isLoading) return null;

  return (
    <Routes>
      <Route index element={<IndexPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/sign-up" element={<SignUpPage />} />
      <Route
        path="/write"
        element={
          <RequireAuth>
            <BoardWritePage />
          </RequireAuth>
        }
      />
      <Route path="/boards/:id" element={<BoardDetailPage />} />
    </Routes>
  );
}

export default App;
