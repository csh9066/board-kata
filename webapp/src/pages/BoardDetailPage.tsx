import { Layout, Typography } from "antd";
import moment from "moment";
import { useQuery } from "react-query";
import { useParams } from "react-router-dom";
import { getBoardDetail } from "../api/boards";
import DefaultTemplate from "../components/DefaultTemplate";

const { Title, Text } = Typography;

export default function BoardDetailPage() {
  const { id } = useParams();

  const { data } = useQuery([`/boards/${id}`, id], () =>
    getBoardDetail(Number(id))
  );

  return (
    <DefaultTemplate>
      <div>
        <Title>{data?.title}</Title>
        <div>
          <Text style={{ marginRight: 5 }}>{data?.author.nickname}</Text>
          <Text type="secondary">{moment(data?.createdAt).fromNow()}</Text>
        </div>
      </div>
      <Layout.Content style={{ paddingTop: 30 }}>
        {data?.content}
      </Layout.Content>
    </DefaultTemplate>
  );
}
