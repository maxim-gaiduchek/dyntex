import React, { useCallback } from 'react';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';
import { useState, useRef, useEffect } from 'react';
import ImageNode from '../Editor/ImageNode'
import 'reactflow/dist/style.css';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Button, Group, Paper, Select, Text } from '@mantine/core';
import PaneMenu from '../Editor/PaneMenu';
import NodeMenu from '../Editor/NodeMenu';

const nodeTypes = {
    imageNode: ImageNode,
  };

const initialEdges = [{ id: 'e1-2', source: '1', target: '2' }];
 
export default function App() {
  // eslint-disable-next-line
  const [info, setInfo] = useState("")
  const reactFlowRef = useRef(null);
  const [lastId, setId] = useState(4)
  const [menu, setMenu] = useState({x: 0, y: 0, hidden: true})
  const [nodeMenu, setNodeMenu] = useState({x: 0, y: 0, hidden: true})


  const initialNodes = [
    { id: '1', position: { x: 150, y: 100 }, data: { label: '1' } },
    { id: '2', position: { x: 150, y: 200 }, data: { label: '2' } },
    { id: '3', position: { x: 250, y: 300 }, data: { name: "Papich", label: '2', image: "" }, type: 'imageNode'}
  ];

  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [opened, setOpened] = useState(false)
  const [nodeOpened, setNodeOpened] = useState(false)
  
  let { id } = useParams();
 
  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges]
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

  const addNode = (x, y, value) => {
    if(value === "Texture"){
      let nds = [...nodes]
      nds.push({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { name: info.name, label: '2', image: "http://localhost:8080/api/media/previews/"+info.previewPath }, type: 'imageNode'})
      setNodes(nds)
      setId(lastId+1)
      setMenu({x: 0, y: 0, hidden: true})
      return
    }
    let nds = [...nodes]
    nds.push({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { label: (lastId+1).toString() } })
    setNodes(nds)
    setId(lastId+1)
    setMenu({x: 0, y: 0, hidden: true})
  }
  
  const onPaneClick = () => {
    setOpened(false)
    setNodeMenu({x: 0, y:0, hidden: true})
    setMenu({x: 0, y: 0, hidden: true})
  }

  useEffect(() => {
    getData()
  },[])

  const onNodeContextMenu = (event, node) => {
    console.log("dasdsd")
    setNodeMenu({y: event.clientX, x: event.clientY, hidden: false, node: node.id})
  }

  const onContextMenu = useCallback((event) => {
    event.preventDefault()
    if(event.target.className !== "react-flow__pane"){
      return 
    }

    setMenu({y: event.clientX, x: event.clientY, hidden: false})
  },setMenu)
 
  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <ReactFlow
        ref={reactFlowRef}
        nodes={nodes}
        edges={edges}
        zoomOnScroll={false}
        zoomOnPinch={false}
        zoomOnDoubleClick={false}
        nodeTypes={nodeTypes}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
        onContextMenu={onContextMenu}
        onPaneClick={onPaneClick}
        onNodeContextMenu={onNodeContextMenu}
      >
        <MiniMap />
        <Background onContextMenu={onNodeContextMenu} />
        <PaneMenu menu={menu} opened={opened} setOpened={setOpened} addNodeProp={addNode}/>
        <NodeMenu menu={nodeMenu} opened={nodeOpened} setOpened={setNodeOpened}/>
      </ReactFlow>
    </div>
  );
}