import { Group, ScrollArea } from '@mantine/core';
import {
  IconAlbum,
  IconCamera,
  IconGauge,
  IconUser,
  IconBookmark,
} from '@tabler/icons-react';
import { LinksGroup } from './NavbarLinksGroup';
import classes from './NavbarNested.module.css';
import { UserButton } from './UserButton';
import { Link } from 'react-router-dom';
import ColorSwitch from './ColorSwitch';

const mockdata = [
  { label: 'Dashboard', icon: IconGauge, link: "/" },
  { label: "All Media", icon: IconAlbum, link: "/media"},
  { label: "Textures", icon: IconCamera, link: "/textures"},
  { label: "Masks", icon: IconAlbum, link: "/masks"},
  { label: "Users", icon: IconUser, link: "/users"},
  { label: "Favourites", icon: IconBookmark, link: "/favourites"},
];

export function NavbarNested() {

  const links = mockdata.map((item) => <LinksGroup {...item} key={item.label} />);
  return (
    
    <nav className={classes.navbar}>
      <div className={classes.header}>
        <Group justify="space-between">
          Dyntex
          {/* <Logo style={{ width: rem(120) }} /> */}
          <ColorSwitch/>
        </Group>
      </div> 

      <ScrollArea style={{paddingTop: 0}} className={classes.links}>
        <div className={classes.linksInner}>{links}</div>
      </ScrollArea>

      <div className={classes.footer}>
        <Link to={"/account"}>
          <UserButton />
        </Link>
      </div>
    </nav>
  );
}