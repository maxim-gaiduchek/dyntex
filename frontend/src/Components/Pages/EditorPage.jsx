import React, { useCallback } from 'react';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';
import { useState, useLayoutEffect, useEffect } from 'react';
import ImageNode from '../Editor/ImageNode'
import 'reactflow/dist/style.css';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Button, Group, Paper, Select, Text } from '@mantine/core';

const nodeTypes = {
    imageNode: ImageNode,
  };

const initialEdges = [{ id: 'e1-2', source: '1', target: '2' }];
 
export default function App() {
  // eslint-disable-next-line
  const [info, setInfo] = useState("")
  const [menu, setMenu] = useState({x: 0, y: 0, hidden: true})

  const initialNodes = [
    { id: '1', position: { x: 150, y: 100 }, data: { label: '1' } },
    { id: '2', position: { x: 150, y: 200 }, data: { label: '2' } },
    { id: '3', position: { x: 250, y: 300 }, data: { name: "Papich", label: '2', image: "" }, type: 'imageNode'}
  ];

  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  let { id } = useParams();
 
  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );

  const getData = async () => {
    const response = await axios.get("http://localhost:5000/initial?sessionid="+id)
      
    setNodes((nds) =>
      initialNodes.map((node) => {
        if (node.id === '3') {
          // when you update a simple type you can just update the value
          node.data.image = "http://localhost:8080/api/media/previews/" + response.data.previewPath
          node.data.name = response.data.name
        }

        return node;
    }))

    setEdges((eds) =>
      initialEdges.map((edge) => {
        return edge;
      })
    );

    setInfo(response.data)
  }

  const addNode = () => {
    let nds = [...nodes]
    nds.push({ id: '5', position: { x: 550, y: 550 }, data: { label: '5' } })
    setNodes(nds)
    setMenu({x: 0, y: 0, hidden: true})
  }

  useEffect(() => {
    getData()
  },[])

  const onNodeContextMenu = (event, node) => {
    console.log("dasdsd")
  }

  const onContextMenu = useCallback((event) => {
    event.preventDefault()
    if(event.target.className !== "react-flow__pane"){
      return 
    }
    setMenu({y: event.clientX, x: event.clientY, hidden: false})
    console.log(event)
  },setMenu)
 
  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        onContextMenu={onContextMenu}
        onPaneClick={() => {setMenu({x: 0, y: 0, hidden: true})}}
        onNodeContextMenu={onNodeContextMenu}
      >
        <Controls />
        <MiniMap />
        <Background onContextMenu={onNodeContextMenu} />
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
              label="Select Node Type"
              placeholder="Node Type"
              data={['Texture', 'Mask', 'Filter', 'Noise']}
            />
            <br/>
            <Button onClick={addNode}>Add node</Button>
          </Paper>
        </Group>
      </ReactFlow>
    </div>
  );
}