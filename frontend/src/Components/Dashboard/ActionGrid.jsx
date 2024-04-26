import {
    Card,
    Text,
    SimpleGrid,
    UnstyledButton,
    Anchor,
    Group,
    useMantineTheme,
  } from '@mantine/core';
  import {
    IconAlbum,
    IconCamera,
    IconUserPentagon,
    IconUser,
    IconBookmark,
  } from '@tabler/icons-react';
  import classes from './ActionGrid.module.css';
  import { useNavigate } from 'react-router-dom';

  const mockdata = [
    { title: 'All Media', icon: IconAlbum, color: 'violet', link: "/media"},
    { title: 'My Profile', icon: IconUserPentagon, color: 'indigo', link: "/account/me" },
    { title: 'Textures', icon: IconCamera, color: 'blue', link: "/textures" },
    { title: 'Masks', icon: IconAlbum, color: 'green', link: "/masks" },
    { title: 'Favourites', icon: IconBookmark, color: 'teal', link: "/favourites" },
    { title: 'Users', icon: IconUser, color: 'cyan', link: "/users" },
  ];
  
  export default function ActionsGrid() {
    const theme = useMantineTheme();
    const navigate = useNavigate();

    const items = mockdata.map((item) => (
      <UnstyledButton onClick={() => navigate(item.link)} key={item.title} className={classes.item}>
        <item.icon color={theme.colors[item.color][6]} size="2rem" />
        <Text size="xs" mt={7}>
          {item.title}
        </Text>
      </UnstyledButton>
    ));
  
    return (
      <Card withBorder radius="md" className={classes.card}>
        <Group justify="space-between">
          <Text className={classes.title}>Services</Text>
          {/* <Anchor size="xs" c="dimmed" style={{ lineHeight: 1 }}>
            + 21 other services
          </Anchor> */}
        </Group>
        <SimpleGrid cols={3} mt="md">
          {items}
        </SimpleGrid>
      </Card>
    );
  }