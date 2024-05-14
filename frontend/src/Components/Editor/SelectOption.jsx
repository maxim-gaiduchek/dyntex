import { Group } from "@mantine/core";
import { IconCheck } from "@tabler/icons-react";

export default function SelectOption(props){

    var {label, value} = props.option
    var checked = props.checked
    return (
        <Group flex={1} gap={"xs"}>
            <img 
            style = {{
                width: 50,
            }}
            alt="preview" src={"http://localhost:8080/api/media/previews/"+props.option.value}/>
            {checked && <IconCheck stroke={1.5} size={18}/>}
            {props.option.label}
        </Group>
    )
}