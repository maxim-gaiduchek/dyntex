import { Group, Paper, Text, Button, Select } from "@mantine/core"
import { useViewport } from 'reactflow';
import { useState } from "react";

export default function PaneMenu(props) {
    const {menu, addNodeProp, opened, setOpened} = props
    const {x, y, zoom} = useViewport();
    const [value, setValue] = useState("")

    const addNode = () => {
        addNodeProp(-x + menu.y, -y + menu.x, value)
    }

    return (
        <Group>
          <Paper style={
              {
                backgroundColor: "var(--mantine-color-dark-4)",
                zIndex: 500,
                position: "absolute",
                display: menu.hidden ? "none" : "block",
                top: menu.x,
                left: menu.y
              }
            } 
            shadow="xs" p="xs">
            <Text>Add Node</Text>
            <br/>
            <Select
              onDropdownOpen={() => setOpened(true)}
              onDropdownClose={() => setOpened(false)}
              dropdownOpened={opened}
              label="Select Node Type"
              placeholder="Node Type"
              value={value}
              onChange={(e) => setValue(e)}
              data={['Texture', 'Mask', 'Filter', 'Noise']}
            />
            <br/>
            <Button disabled={value===""} onClick={addNode}>Add node</Button>
          </Paper>
        </Group>
    )
}