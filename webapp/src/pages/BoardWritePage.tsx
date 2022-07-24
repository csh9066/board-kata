import { Form } from "antd";
import { useNavigate } from "react-router-dom";
import { createBoard, CreateBoardData } from "../api/boards";
import BoardWirteForm from "../components/BoardWirteForm";
import DefaultTemplate from "../components/DefaultTemplate";

export default function BoardWritePage() {
  const [form] = Form.useForm();

  const navigate = useNavigate();

  const onWriteBoard = async (data: CreateBoardData) => {
    try {
      await createBoard(data);
      navigate("/");
    } catch (e) {
      console.log(e);
    }
  };

  return (
    <DefaultTemplate>
      <BoardWirteForm form={form} onSubmit={onWriteBoard} />
    </DefaultTemplate>
  );
}
