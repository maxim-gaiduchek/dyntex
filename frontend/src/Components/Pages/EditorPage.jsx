import React, { useCallback } from 'react';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';
import { useHotkeys } from 'react-hotkeys-hook'
import { useState, useRef, useEffect } from 'react';
import ImageNode from '../Editor/ImageNode'
import MaskNode from '../Editor/MaskNode';
import OutputNode from '../Editor/OutputNode';
import 'reactflow/dist/style.css';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { Button, Group, Paper, Select, Text } from '@mantine/core';
import PaneMenu from '../Editor/PaneMenu';
import NodeMenu from '../Editor/NodeMenu';
import { notifications } from '@mantine/notifications';
import { useComputedColorScheme } from '@mantine/core';
import { ControlButton } from 'reactflow';
import { getIncomers, getOutgoers, getConnectedEdges } from 'reactflow';


const nodeTypes = {
    imageNode: ImageNode,
    maskNode: MaskNode,
    outputNode: OutputNode 
  };

const initialEdges = [{ id: 'e1-2', source: '2', target: '1' }];
 
export default function App() {
  // eslint-disable-next-line
  const [info, setInfo] = useState("")
  const reactFlowRef = useRef(null);
  const [lastId, setId] = useState(4)
  const [menu, setMenu] = useState({x: 0, y: 0, hidden: true})
  const [nodeMenu, setNodeMenu] = useState({x: 0, y: 0, hidden: true})
  const computedColorScheme = useComputedColorScheme('light', { getInitialValueInEffect: true });


  const initialNodes = [
    { id: '1', position: { x: 450, y: 100 }, data: { label: '1', image: "" }, type: "outputNode"},
    { id: '2', position: { x: 150, y: 100 }, data: { name: "Papich", label: '2', image: "" }, type: 'imageNode'}
  ];

  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [opened, setOpened] = useState(false)
  const [videos, setVideos] = useState([])
  const [masks, setMasks] = useState([])
  const [nodeOpened, setNodeOpened] = useState(false)

  const onClickElementDelete = (e) => {
    console.log(e)
  }

  useHotkeys('delete', () => {
    setEdges((eds) => eds.filter((e) => e.selected === undefined));
  })
  
  let { id } = useParams();
 
  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges]
  );

  const getData = async () => {
    const response = await axios.get("http://localhost:5000/initial?sessionid="+id)
      
    setNodes((nds) =>
      initialNodes.map((node) => {
        if (node.id === '2' || node.id === "1") {
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

  const deleteNode = (node) => {
    // setEdges(
    //   edges.map((edge) => {
    //     if(edge.source !== node.id && edge.target !== node.id){
    //       return edge
    //     }
    //   })
    // );
    if(node.id === "1"){
      notifications.show({
        title: 'Error',
        message: 'Can\' t delete output node. 🚫',
        color: "red"
      })
      setNodeMenu({x: 0, y:0, hidden: true, node: {}})
      return
    }
    setEdges((edg) => edg.filter((ed) => ed.source !== node.id && ed.target !== node.id))
    setNodes((nds) => nds.filter((nd) => nd.id !== node.id))

    setNodeMenu({x:0, y:0, hidden: true, node: {}})
  }

  const addNode = (x, y, value, path="") => {
    if(value === "Texture"){
      var v = videos.filter((video) => video.previewPath===path)
      if(v.length !== 1){
        return
      }
      var vNow = v[0]
      let nds = [...nodes]
      nds.push({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { name: vNow.name, label: '2', image: "http://localhost:8080/api/media/previews/"+path }, type: 'imageNode'})
      setNodes(nds)
      setId(lastId+1)
      setMenu({x: 0, y: 0, hidden: true})
      return
    }
    if(value === "Mask"){
      var v = masks.filter((video) => video.path===path)
      if(v.length !== 1){
        return
      }
      let nds =[...nodes]
      nds.push({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { name: v[0].name, label: '2', image: "http://localhost:8080/api/media/previews/"+path }, type: 'maskNode'})
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

  const getVideos = async () => {
    try{
      const response = await axios.get("http://localhost:8080/api/videos?pageSize=200")

      setVideos(response.data.videos)
    }catch(e){}
  }

  const getMasks = async () => {
    try{
      const response = await axios.get("http://localhost:8080/api/masks?pageSize=200")
      
      setMasks(response.data.masks)
    }catch(e){}
  }

  useEffect(() => {
    getData()
    getVideos()
    getMasks()
  },[])

  const onNodeContextMenu = (event, node) => {
    console.log("dasdsd")
    setNodeMenu({y: event.clientX, x: event.clientY, hidden: false, node: node})
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
        <PaneMenu color={computedColorScheme} menu={menu} videos={
          videos.map((video) => {return {value: video.previewPath, label: video.name}})
        } 
        masks = {
          masks.map((mask) => {return {value: mask.path, label: mask.name}})
        }
        opened={opened} setOpened={setOpened} addNodeProp={addNode}/>
        {/* <Controls showInteractive={false}>
          <ControlButton onClick={onClickElementDelete}>
            dasdsads
          </ControlButton>
        </Controls> */}
        <NodeMenu color={computedColorScheme} menu={nodeMenu} opened={nodeOpened} deleteNodeP={deleteNode} setOpened={setNodeOpened}/>
      </ReactFlow>
    </div>
  );
}