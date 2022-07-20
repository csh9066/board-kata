import {
  Avatar,
  Button,
  Dropdown,
  Layout,
  Menu,
  Space,
  Typography,
} from "antd";
import { CaretDownOutlined } from "@ant-design/icons";
import { Link } from "react-router-dom";
import useAuth from "../hooks/useAuth";

function menu(logout: any) {
  return (
    <Menu
      items={[
        {
          key: 1,
          label: "로그 아웃",
          onClick: logout,
        },
      ]}
    />
  );
}

function Header() {
  const { isLoggedIn, logout, me } = useAuth();

  return (
    <Layout.Header
      style={{
        background: "transparent",
        display: "flex",
        justifyContent: "space-between",
      }}
    >
      <Link to="/">
        <Typography.Text strong>BOARD KATA</Typography.Text>
      </Link>
      <div>
        {isLoggedIn ? (
          <Space>
            <Button type="default" shape="round">
              <Link to="/write">새 글 작성</Link>
            </Button>
            <Dropdown overlay={menu(logout)}>
              <Space>
                <Avatar>{me?.nickname[0]}</Avatar>
                <CaretDownOutlined />
              </Space>
            </Dropdown>
          </Space>
        ) : (
          <Button type="primary">
            <Link to="/login">로그인</Link>
          </Button>
        )}
      </div>
    </Layout.Header>
  );
}

export default Header;
