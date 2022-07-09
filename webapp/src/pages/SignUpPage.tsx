import { Button, Form, Input, message, Typography } from "antd";
import { Link, useNavigate } from "react-router-dom";
import { createUser } from "../api/users";
import AuthTemplate from "../components/AuthTemplate";
import { CreateUserData } from "../types";

type SignUpForm = {
  email: string;
  password: string;
  passwordCheck: string;
  nickname: string;
};

export default function SignUpPage() {
  const navigate = useNavigate();

  const onSignup = async (form: SignUpForm) => {
    try {
      const signupData: CreateUserData = {
        email: form.email,
        password: form.password,
        nickname: form.nickname,
      };

      await createUser(signupData);

      message.success("회원가입 성공");

      navigate("/login");
    } catch (e: any) {
      if (e?.response?.data.message) {
        message.error(e?.response?.data.message);
        return;
      }

      console.error(e);
    }
  };

  const [form] = Form.useForm();

  return (
    <AuthTemplate
      title="회원 가입"
      footer={
        <Typography.Text>
          이미 계정이 있으면 <Link to="/login">로그인</Link>
        </Typography.Text>
      }
    >
      <Form onFinish={onSignup} form={form}>
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

        <Form.Item
          name="passwordCheck"
          rules={[
            { required: true, message: "패스워드를 입력해주세요" },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue("password") === value) {
                  return Promise.resolve();
                }
                return Promise.reject(
                  new Error("패스워드가 일치하지 않습니다.")
                );
              },
            }),
          ]}
        >
          <Input size="large" type="password" placeholder="패스워드 체크" />
        </Form.Item>

        <Form.Item
          name="nickname"
          rules={[{ required: true, message: "닉네임을 입력해주세요" }]}
        >
          <Input size="large" type="text" placeholder="닉네임" />
        </Form.Item>

        <Button block type="primary" size="large" htmlType="submit">
          회원 가입
        </Button>
      </Form>
    </AuthTemplate>
  );
}
