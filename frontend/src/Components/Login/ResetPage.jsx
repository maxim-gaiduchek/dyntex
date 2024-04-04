import {
    TextInput,
    Anchor,
    Paper,
    Title,
    Text,
    Container,
    Group,
    Button,
  } from '@mantine/core';
  import { IconExclamationCircle } from '@tabler/icons-react';
  import classes from './LoginPage.module.css';
  import { Link } from 'react-router-dom';
  import React from 'react';
  import { notifications } from '@mantine/notifications';

  export default function ResetPage() {
    const [email, setEmail] = React.useState("");
    const [error, setError] = React.useState([false])

    const checkEmail = (event) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        let text = event.target.value;

        let errcpy = [...error];
        
        errcpy[0] = text === "" ? false : !emailRegex.test(text);
        setError(errcpy);
        setEmail(text);
    }

    const login = () => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if(!emailRegex.test(email)){
            notifications.show({
                title: 'Invalid input',
                color: 'red',
                icon: <IconExclamationCircle/>,
                autoClose: 2000,
                message: 'Please fill out all the required data. 🤥',
            })
            return;
        }

        notifications.show({
            title: 'Account not found',
            color: 'red',
            icon: <IconExclamationCircle/>,
            autoClose: 4000,
            message: 'Authentication Failed: Please ensure that the email and password provided are correct. Make sure there are no typos and try again. 😅',
        })
    }

    return (
      <Container size={420} my={40}>
        <Title ta="center" className={classes.title}>
          Password Recovery
        </Title>
        <Text c="dimmed" size="sm" ta="center" mt={5}>
          Do not have an account yet?{' '}
          <Anchor size="sm" component="button">
            <Link to="/register">Create account</Link>
          </Anchor>
        </Text>
  
        <Paper withBorder shadow="md" p={30} mt={30} radius="md">
          <TextInput label="Email" error={error[0]} value={email} onChange={checkEmail} placeholder="you@example.com" required />
          <Group justify="space-between">
          </Group>
          <Button onClick={login} disabled={(email === "") ? true : error[0]} fullWidth mt="xl">
            Sign in
          </Button>
        </Paper>
      </Container>
    );
  }