import { Title, Text, Button, Container, Group } from '@mantine/core';
import classes from './NotFoundTitle.module.css';
import { Link } from 'react-router-dom';

export default function ServerDown() {
  return (
    <Container className={classes.root}>
      <div className={classes.label}>503</div>
      <Title className={classes.title}>Server is currently down.</Title>
      <Text c="dimmed" size="lg" ta="center" className={classes.description}>
        We're sorry for the inconvenience. Our server is currently experiencing some issues. 
        Please try again later.
      </Text>
      <Group justify="center">
        <Link to={"/"}>
            <Button variant="subtle" size="md">
                Take me back to home page
            </Button>
        </Link>
      </Group>
    </Container>
  );
}