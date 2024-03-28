import { Group, Code, ScrollArea } from '@mantine/core';
import {
  IconNotes,
  IconCalendarStats,
  IconGauge,
  IconPresentationAnalytics,
  IconFileAnalytics,
  IconAdjustments,
  IconLock,
} from '@tabler/icons-react';
import { LinksGroup } from './NavbarLinksGroup';
import classes from './NavbarNested.module.css';
import { UserButton } from './UserButton';
import { Link } from 'react-router-dom';
import ColorSwitch from './ColorSwitch';

const mockdata = [
  { label: 'Dashboard', icon: IconGauge, link: "/" },
  {
    label: 'Market news',
    icon: IconNotes,
    link: "/secondary",
    initiallyOpened: false,
    links: [
      { label: 'Overview', link: '/secondary' },
      { label: 'Forecasts', link: '/secondary' },
      { label: 'Outlook', link: '/secondary' },
      { label: 'Real time', link: '/secondary' },
    ],
  },
  {
    label: 'Releases',
    link: "/secondary",
    icon: IconCalendarStats,
    links: [
      { label: 'Upcoming releases', link: '/secondary' },
      { label: 'Previous releases', link: '/secondary' },
      { label: 'Releases schedule', link: '/secondary' },
    ],
  },
  { label: 'Analytics', icon: IconPresentationAnalytics, link:"/secondary" },
  { label: 'Contracts', icon: IconFileAnalytics, link:"/secondary" },
  { label: 'Settings', icon: IconAdjustments, link:"/secondary" },
  {
    label: 'Security',
    icon: IconLock,
    link: "/",
    links: [
      { label: 'Enable 2FA', link: '/' },
      { label: 'Change password', link: '/' },
      { label: 'Recovery codes', link: '/' },
    ],
  },
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