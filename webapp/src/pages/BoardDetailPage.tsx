import { css } from "@emotion/css";
import { Button, Layout, Popconfirm, Space, Typography } from "antd";
import moment from "moment";
import { useQuery } from "react-query";
import { Link, useNavigate, useParams } from "react-router-dom";
import { delteBoard, getBoardDetail } from "../api/boards";
import DefaultTemplate from "../components/DefaultTemplate";
import useAuth from "../hooks/useAuth";

const { Title, Text } = Typography;

export default function BoardDetailPage() {
  const { id } = useParams();
  const { data } = useQuery([`/boards/${id}`, id], () =>
    getBoardDetail(Number(id))
  );

  const { me } = useAuth();

  const navigate = useNavigate();
  const onClickDeleteBtn = async () => {
    try {
      await delteBoard(Number(id));
      navigate("/");
    } catch (e: any) {
      console.log(e.response.data);
    }
  };

  if (!data) return null;

  return (
    <DefaultTemplate>
      <div>
        <Title>{data?.title}</Title>
        <div className={HeaderDescriptionCss}>
          <Space>
            <Text>{data?.author.nickname}</Text>
            <Text type="secondary">{moment(data?.createdAt).fromNow()}</Text>
          </Space>
          {me?.id === data.author.id && (
            <Space>
              <Popconfirm
                title="정말 삭제 하시겠습니까?"
                okText="네"
                cancelText="아니요"
                onConfirm={onClickDeleteBtn}
              >
                <Button>삭제하기</Button>
              </Popconfirm>
              <Button type="primary">
                <Link to="">수정하기</Link>
              </Button>
            </Space>
          )}
        </div>
      </div>
      <Layout.Content
        style={{ paddingTop: 30 }}
        dangerouslySetInnerHTML={{ __html: data?.content }}
      />
    </DefaultTemplate>
  );
}

const HeaderDescriptionCss = css`
  display: flex;
  justify-content: space-between;
  align-items: center;
`;
