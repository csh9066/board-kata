import "antd/dist/antd.min.css";
import { Route, Routes } from "react-router-dom";
import BoardDetailPage from "./pages/BoardDetailPage";
import BoardWritePage from "./pages/BoardWritePage";
import IndexPage from "./pages/IndexPage";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";

function App() {
  return (
    <Routes>
      <Route index element={<IndexPage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/sign-up" element={<SignUpPage />} />
      <Route path="/write" element={<BoardWritePage />} />
      <Route path="/boards/:id" element={<BoardDetailPage />} />
    </Routes>
  );
}

export default App;
