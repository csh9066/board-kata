import { css } from "@emotion/css";
import { Divider, Typography } from "antd";
import React from "react";

type Props = {
  title: string;
  children: React.ReactNode; // form
  footer: React.ReactElement;
};

export default function AuthTemplate({ title, footer, children }: Props) {
  return (
    <div
      className={css`
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        right: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        background: #eee;
      `}
    >
      <div
        className={css`
          width: 360px;
          padding: 40px 20px;
          background: white;
        `}
      >
        <Typography.Title style={{ textAlign: "center" }}>
          {title}
        </Typography.Title>
        {children}
        <Divider />
        <div
          className={css`
            display: flex;
            flex-direction: row-reverse;
          `}
        >
          <Typography.Text>{footer}</Typography.Text>
        </div>
      </div>
    </div>
  );
}
