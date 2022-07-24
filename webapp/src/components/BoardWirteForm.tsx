import { css } from "@emotion/css";
import { Button, Form, FormInstance, Input } from "antd";
import ReactQuill from "react-quill";
import { CreateBoardData } from "../api/boards";

const modules = {
  toolbar: [
    [{ header: [1, 2, false] }],
    ["bold", "italic", "underline", "strike", "blockquote"],
    [
      { list: "ordered" },
      { list: "bullet" },
      { indent: "-1" },
      { indent: "+1" },
    ],
    ["link", "image"],
    ["clean"],
  ],
};

type Props = {
  form: FormInstance<CreateBoardData>;
  onSubmit: (data: CreateBoardData) => void;
};

export default function BoardWirteForm({ form, onSubmit }: Props) {
  return (
    <Form form={form} onFinish={onSubmit}>
      <Form.Item name="title">
        <Input size="large" placeholder="타이틀"></Input>
      </Form.Item>
      <Form.Item name="content">
        <ReactQuill modules={modules} className={QuillEditorStyles} />
      </Form.Item>
      <Form.Item>
        <Button htmlType="submit" type="primary" block size="large">
          작성하기
        </Button>
      </Form.Item>
    </Form>
  );
}

const QuillEditorStyles = css`
  .ql-container {
    height: 300px;
  }
`;
