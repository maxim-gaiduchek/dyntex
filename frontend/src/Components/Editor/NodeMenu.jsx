import { Group, Paper, Text, Button, Select } from "@mantine/core"
import { useState } from "react";
import { IconTrashXFilled } from "@tabler/icons-react";

export default function NodeMenu(props) {
    const {menu, opened, setOpened, deleteNodeP} = props
    const [value, setValue] = useState("")

    const deleteNode = () => {
        deleteNodeP(menu.node)
        // addNodeProp(-x + menu.y, -y + menu.x, value)
    }

    return (
        <Group>
          <Paper style={
              {
                backgroundColor: props.color === "dark" ? "var(--mantine-color-dark-4)" : "white",
                zIndex: 500,
                position: "absolute",
                display: menu.hidden ? "none" : "block",
                top: menu.x,
                left: menu.y
              }
            } 
            shadow="xs" p="xs">
            <Text>Node Menu</Text>
            <br/>
            <Button onClick={deleteNode} rightSection={<IconTrashXFilled/>} color="red" >Delete node</Button>
          </Paper>
        </Group>
    )
}