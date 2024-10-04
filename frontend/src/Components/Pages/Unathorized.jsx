import { Title, Text, Button, Container, Group } from '@mantine/core';
import classes from './NotFoundTitle.module.css';
import { Link } from 'react-router-dom';

export default function Unauthorized() {
  return (
    <Container className={classes.root}>
      <div className={classes.label}>403</div>
      <Title className={classes.title}>Email not verified</Title>
      <Text c="dimmed" size="lg" ta="center" className={classes.description}>
        Email not verified. Please verify your email before logging
      </Text>
      <Group justify="center">
      </Group>
    </Container>
  );
}