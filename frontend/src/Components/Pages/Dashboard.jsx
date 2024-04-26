import Stats from "../Dashboard/Stats"
import ActionsGrid from "../Dashboard/ActionGrid"
import { Group } from "@mantine/core"
export default function Dashboard() {

    return (
        <div>
            <h2>Dashboard</h2>
            <Stats/>
            <Group grow style={{width: "100%", maxWidth: 400, margin: "0 auto"}} justify="center">
                <ActionsGrid/>
            </Group>
        </div>
    )
}