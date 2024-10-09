import { UnstyledButton, Group, Avatar, Text, rem } from '@mantine/core';
import { IconChevronRight } from '@tabler/icons-react';
import classes from './UserButton.module.css';
import { useState, useEffect } from 'react';
import { callApi, getUser } from '../../utils';
import { Link } from 'react-router-dom';
import { useCookies } from 'react-cookie';
export function UserButton() {
  const [cookies, setCookie, removeCookie] = useCookies(['dyntex']);
  const [user, setUser] = useState(undefined);

  useEffect(() => {
    getUser(cookies.token).then((u) => setUser(u.data));
    // getUser(cookies.token).then((u) => setUser(u.data));
    // callApi("/api/users/authenticated", "get", {}, cookies.token).then((u) => setUser(u.data));
  }, [])

  return (
    <Link to={"/account/me"}>
      <UnstyledButton className={classes.user}>
        <Group>
          <Avatar
            src={cookies?.avatar || "/avatar.png"}
            radius="xl"
          />

          <div style={{ flex: 1 }}>
            <Text size="sm" fw={500}>
              {user?.name || "Loading..."}
            </Text>

            <Text c="dimmed" size="xs">
              {user?.email || "Loading..."}
            </Text>
          </div>

          <IconChevronRight style={{ width: rem(14), height: rem(14) }} stroke={1.5} />
        </Group>
      </UnstyledButton>      
    </Link>
  );
}