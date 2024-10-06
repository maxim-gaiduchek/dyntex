import { Group } from "@mantine/core";
import { IconCheck } from "@tabler/icons-react";
import BaseUrl from "../../BaseUrl";

export default function SelectOption(props){

    var {label, value} = props.option
    var checked = props.checked
    return (
        <Group flex={1} gap={"xs"}>
            <img 
            style = {{
                width: 50,
            }}
            alt="preview" src={BaseUrl+"/api/media/previews/"+props.option.value}/>
            {checked && <IconCheck stroke={1.5} size={18}/>}
            {props.option.label}
        </Group>
    )
}