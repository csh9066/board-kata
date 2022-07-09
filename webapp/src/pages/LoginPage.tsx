import { Button, Form, Input, message } from "antd";
import { Link, useNavigate } from "react-router-dom";
import { login } from "../api/auth";
import AuthTemplate from "../components/AuthTemplate";

type LoginForm = {
  email: string;
  password: string;
};

export default function LoginPage() {
  const navigate = useNavigate();

  const onLogin = async (form: LoginForm) => {
    try {
      await login(form.email, form.password);
      navigate("/");
    } catch (e: any) {
      if (e?.response?.data.message) {
        message.error(e?.response?.data.message);
        return;
      }

      console.error(e);
    }
  };

  return (
    <AuthTemplate
      title="로그인"
      footer={<Link to={"/sign-up"}>회원 가입</Link>}
    >
      <Form onFinish={onLogin}>
        <Form.Item
          name="email"
          rules={[{ required: true, message: "이메일을 입력해주세요" }]}
        >
          <Input size="large" type="email" placeholder="이메일" />
        </Form.Item>

        <Form.Item
          name="password"
          rules={[
            { required: true, message: "패스워드를 입력해주세요" },
            {
              min: 8,
              max: 16,
              message: "비밀번호는 8 ~ 16 값입니다.",
            },
          ]}
        >
          <Input size="large" type="password" placeholder="패스워드"></Input>
        </Form.Item>

        <Button block type="primary" size="large" htmlType="submit">
          로그인
        </Button>
      </Form>
    </AuthTemplate>
  );
}
