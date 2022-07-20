import { Avatar, List } from "antd";
import QueryString from "qs";
import { useEffect, useState } from "react";
import { useQuery } from "react-query";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { Board, getBoards } from "../api/boards";

export default function BoardList() {
  const location = useLocation();

  const [page, setPage] = useState(0);

  useEffect(() => {
    const { page }: { page?: number } = QueryString.parse(location.search, {
      ignoreQueryPrefix: true,
    });

    setPage(Number(page ? page : 0));
  }, [location.search]);

  const { data } = useQuery(["getBoards", page], () => getBoards(page), {
    keepPreviousData: true,
  });

  const navigate = useNavigate();

  const onChangePagination = (page: number) => {
    const realPage = page - 1;
    navigate({
      search: `?page=${realPage}`,
    });
  };

  return (
    <List
      itemLayout="horizontal"
      dataSource={data?.results}
      pagination={{
        pageSize: 10,
        current: page + 1,
        total: data?.totalCount,
        onChange: onChangePagination,
      }}
      renderItem={(item: Board) => (
        <List.Item>
          <List.Item.Meta
            avatar={<Avatar />}
            title={
              <Link to={`./boards/${item.id}`}>{item.author.nickname}</Link>
            }
            description={item.title}
          />
        </List.Item>
      )}
    />
  );
}
