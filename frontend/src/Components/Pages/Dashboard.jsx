import Stats from "../Dashboard/Stats"
import ActionsGrid from "../Dashboard/ActionGrid"
import { Group, LoadingOverlay } from "@mantine/core"
import { useEffect } from "react"
import axios from "axios"
import { useState } from "react"
import BaseUrl from "../../BaseUrl"
export default function Dashboard() {
    const [stats, setStats] = useState(null)

    const getStats = async () => {
        try{
            const response = await axios.get(BaseUrl+"/api/statistics")
            setStats(response.data)
        }catch(e){
            console.log(e)
        }
    }

    useEffect(() => {
        getStats()
    }, [])
    return (
        <>
            {
                stats !== null ?
                <div>
                    <h2>Dashboard</h2>
                    <Stats stats={stats} />
                    <Group grow style={{width: "100%", maxWidth: 400, margin: "0 auto"}} justify="center">
                        <ActionsGrid/>
                    </Group>
                </div>
                :
                <div style={{width: "100%", height: "calc(100vh - 20px)", marginTop: 5}}>
                    <LoadingOverlay visible={true} zIndex={1000} style={{marginTop: 5}} overlayProps={{ radius: "sm", blur: 2 }}/>
                </div>
            }
        </>
    )
}