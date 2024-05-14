import { Group, Paper, Text, Button, Select } from "@mantine/core"
import { useViewport } from 'reactflow';
import { useState } from "react";
import { Modal } from "@mantine/core";
import SelectOption from "./SelectOption";

export default function PaneMenu(props) {
    const {menu, addNodeProp, opened, setOpened, videos} = props
    const {x, y, zoom} = useViewport();
    const [value, setValue] = useState("")
    const [tValue, setTValue] = useState({label: ""})
    const [mValue, setMValue] = useState({label: ""})
    const [modalOpened, setModalOpened] = useState(false)

    const addNode = () => {
      console.log(props.masks)
      if(value === "Texture" || value === "Mask"){
        setModalOpened(true)
        return
      }
        addNodeProp(-x + menu.y, -y + menu.x, value)
    }

    const addTexture = () => {
      setModalOpened(false)
      addNodeProp(-x + menu.y, -y + menu.x, value, value === "Texture" ? tValue : mValue)
    }

    return (
        <Group>
          <Modal opened={modalOpened} onClose={() => setModalOpened(false)} title={"Add "+value}>
            <Select
              label="Texture"
              value={value==="Mask" ? mValue : tValue}
              placeholder="Select a texture"
              styles={{ dropdown: { maxHeight: 200, overflowY: 'auto' } }}
              searchable
              onChange={(e, b) => {
                if(b === null){
                  return
                }
                if(value==="Mask"){
                  setMValue(b.value)
                }else{
                  setTValue(b.value)
                }
              }}
              data={value === "Mask" ? props.masks : props.videos}
              renderOption={SelectOption}
            />
            <br/>
            <Button onClick={addTexture} disabled={
              value === "Texture" ? tValue.label==="" : mValue.label===""
            }>Add {value}</Button>
          </Modal>
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