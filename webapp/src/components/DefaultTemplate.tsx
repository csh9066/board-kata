import { Layout } from "antd";
import React from "react";
import Header from "./Header";

type Props = {
  children: React.ReactNode;
};

export default function DefaultTemplate({ children }: Props) {
  return (
    <Layout>
      <Header />
      <Layout.Content
        style={{
          background: "#fff",
          padding: 10,
          height: "100%",
        }}
      >
        <div
          style={{
            width: 768,
            margin: "0 auto",
          }}
        >
          {children}
        </div>
      </Layout.Content>
    </Layout>
  );
}
