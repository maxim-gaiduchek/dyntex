import { ActionIcon } from "@mantine/core"
import { IconShare } from "@tabler/icons-react"
import { useMantineTheme } from "@mantine/core"
import { notifications } from '@mantine/notifications';

export function ShareButton(props){
    const theme = useMantineTheme()
    return (
        <ActionIcon variant="default" radius={"md"} size={36} onClick={() => {
            navigator.clipboard.writeText(props.url)
            notifications.show({
                title: 'Link Copied',
                message: 'The link has been copied to clipboard 📋',
                color: theme.colors.blue[6]
                })
        }}>
            <IconShare size={24} color={theme.colors.blue[6]}/>
        </ActionIcon>
    )
}