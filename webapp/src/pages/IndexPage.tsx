import { Button, Layout, Typography } from "antd";
import React from "react";
import { Link } from "react-router-dom";

export default function IndexPage() {
  return (
    <Layout>
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
          <Button type="primary">
            <Link to="/login">로그인</Link>
          </Button>
        </div>
      </Layout.Header>
      <Layout.Content></Layout.Content>
    </Layout>
  );
}
