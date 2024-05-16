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
import FilterNode from '../Editor/FilterNode';
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

//TODO: don't use states in filter nodes, create a react hook in parent element that will update the filter nodes

const nodeTypes = {
    imageNode: ImageNode,
    maskNode: MaskNode,
    outputNode: OutputNode,
    filterNode: FilterNode
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
    { id: '2', position: { x: 150, y: 100 }, data: { name: "Papich", label: '2', path: "", image: "" }, type: 'imageNode'}
  ];

  const [nodes, setNodes, onNodesChange] = useNodesState([]);
  const [edges, setEdges, onEdgesChange] = useEdgesState([]);
  const [opened, setOpened] = useState(false)
  const [videos, setVideos] = useState([])
  const [masks, setMasks] = useState([])
  const [nodeOpened, setNodeOpened] = useState(false)

  useHotkeys('delete', () => {
    setEdges((eds) => eds.filter((e) => e.selected !== true));
  })
  
  let { id } = useParams();
 
  const onConnect = useCallback(
    (params) => {setEdges((eds) => addEdge(params, eds))},
    [setEdges]
  );

  const getData = async () => {
    const response = await axios.get("http://localhost:5000/initial?sessionid="+id)
    setNodes((nds) =>
      initialNodes.map((node) => {
        if (node.id === '2' || node.id === "1") {
          node.data.image = "http://localhost:8080/api/media/previews/" + response.data.previewPath
          node.data.path = response.data.path
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
    if(node.id === "1"){
      notifications.show({
        title: 'Error',
        message: 'Can\' t delete output node. 🚫',
        color: "red"
      })
      setNodeMenu({x: 0, y:0, hidden: true, node: {}})
      return
    }
    var a = false;
    setEdges((edg) => {
      
      const filteredEdges = edg.filter((ed) => ed.source !== node.id && ed.target !== node.id);
      const filterNodeIds = nodes.filter((nd) => nd.type === 'filterNode').map((nd) => nd.id);
      //TODO: FIX TO ONLY UPDATE FILTER NODES THAT ARE CONNECTED TO THE DELETED NODE
      const outgoing = getOutgoers(node, nodes, edges).map((a) => a.id)
      const isPointingToFilterNode = filterNodeIds.some((id) => filteredEdges.some((ed) => ed.target === id));
      if (outgoing.length !== 0) {
        a = true
        var updatedNodes = nodes.map((nd) => {
            if (nd.type === 'filterNode' && outgoing.includes(nd.id)){
            nd.data = {
              ...nd.data,
              updateFiler: updateFilter,
              processed: false
            };
            }
          return nd;
        });
        setNodes(updatedNodes.filter((nd) => nd.id !== node.id));
      }
      return filteredEdges;
    });
    setEdges((edg) => edg.filter((ed) => ed.source !== node.id && ed.target !== node.id))
    if(!a){
      setNodes((nds) => nds.filter((nd) => nd.id !== node.id))
    }
    setNodeMenu({x:0, y:0, hidden: true, node: {}})
  }

  const updateData = (id, strength, swap) => {
    const updatedNodes = nodes.map((nd) => {
      if(nd.id === id){
        nd.data = {
          ...nd.data,
          strength: strength,
          swap: swap,
          updateData: updateData,
          updateFilter: updateFilter
        }
      }
      return nd
    })
    setNodes(updatedNodes)
  }

  const addNodeM = (x, y, value, path="") => {
    if(value === "Texture"){
      var v = videos.filter((video) => video.previewPath===path)
      if(v.length !== 1){
        return
      }
      var vNow = v[0]
      let nds = [...nodes]
      const newNode = ({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { name: vNow.name, label: '2', path: v[0].path,image: "http://localhost:8080/api/media/previews/"+path }, type: 'imageNode'})
      // setNodes(nds)
      setNodes((nds) => nds.concat(newNode));
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
      const newNode = ({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { name: v[0].name, label: '2', path: path,image: "http://localhost:8080/api/media/previews/"+path }, type: 'maskNode'})
      setNodes((nds) => nds.concat(newNode))
      setId(lastId+1)
      setMenu({x: 0, y: 0, hidden: true})
      return
    }

    if(value === "Filter"){
      const newNode = ({ id: (lastId+1).toString(), position: { x: x, y: y }, data: { updateData: updateData, updateFilter: updateFilter, id: (lastId+1).toString(), image: nodes[0].data.image, processed: false, strength: 80, swap: false }, type: 'filterNode', dragHandle: ".draggable"})
      setNodes((nds) => nds.concat(newNode));
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
    setNodeMenu({y: event.clientX, x: event.clientY, hidden: false, node: node})
  }

  const onContextMenu = useCallback((event) => {
    event.preventDefault()
    if(event.target.className !== "react-flow__pane"){
      return 
    }

    setMenu({y: event.clientX, x: event.clientY, hidden: false})
  },setMenu)

  const validate = async (e) => {
    if(nodes === undefined){
      return true
    }
    var a = nodes.findIndex((nd) => nd.id === e.target)
    var incomers = getIncomers(nodes[a], nodes, edges)
    return incomers.length <= (e.target === "1" ? 0 : 1)
  }

  const updateFilter = async (node, mE, strength, newPath="", swap=false) => {
    const outgoers = getOutgoers(node, nodes, mE)
    const incomers = getIncomers(node, nodes, mE)

    if(incomers.length === 2){
      if(!swap){
        var videoPath = incomers[0].type === 'filterNode' ? incomers[0].data.image.split('/').pop() : incomers[0].data.path;
        var imagePath = incomers[1].type === 'filterNode' ? incomers[1].data.image.split('/').pop() : incomers[1].data.path;
      }else {
        var videoPath = incomers[1].type === 'filterNode' ? incomers[1].data.image.split('/').pop() : incomers[1].data.path;
        var imagePath = incomers[0].type === 'filterNode' ? incomers[0].data.image.split('/').pop() : incomers[0].data.path;
      }

      if(newPath === ""){
        if(incomers[0].type === 'filterNode'){
          videoPath = incomers[0].data.image.split('/').pop().split("?")[0]
        }else{
          imagePath = incomers[1].data.image.split('/').pop().split("?")[0]
        }
      }

      const name = node.data.image.split("/").pop().split(".")[0];

      const response = await axios.get(`http://localhost:5000/filter?video_path=${videoPath}&image_path=${imagePath}&strength=${strength}&name=${name}`)
      setNodes((nds) =>
        nodes.map((nd) => {
          console.log(outgoers)
          if(outgoers.length === 1){
            if(outgoers[0].id === nd.id && nd.type === 'outputNode'){
              nd.data = {
                ...nd.data,
                processed: true,
                image: "http://localhost:8080/api/media/previews/" + response.data.image_path + "?time=" + new Date().getTime()
              };
              return nd;
            }
            if(outgoers[0].id === nd.id && nd.type === 'filterNode'){
              updateFilter(nd, nd.data.edges, nd.data.strength/100, response.data.image_path + new Date().getTime(), nd.data.swap)
            }
          }
          if (nd.id === node.id) {
            nd.data = {
              ...nd.data,
              strength: strength*100,
              swap: swap,
              processed: true,
              image: "http://localhost:8080/api/media/previews/" + response.data.image_path + "?time=" + new Date().getTime()
            };
          }

          return nd;
      }))
    }
  }

  const handleConnect = async (e) => {
    var a = nodes.findIndex((nd) => nd.id === e.target)
    var b = nodes.findIndex((nd) => nd.id === e.source)

    var incomers = getIncomers(nodes[a], nodes, edges)
    
    if(incomers.length === 1){

      const videoPath = incomers[0].type === 'filterNode' ? incomers[0].data.image.split('/').pop() : incomers[0].data.path;
      const imagePath = nodes[b].type === 'filterNode' ? nodes[b].data.image.split('/').pop() : nodes[b].data.path;

      const strength = 0.8; 

      const response = await axios.get(`http://localhost:5000/filter?video_path=${videoPath}&image_path=${imagePath}&strength=${strength}`)

      onConnect(e);

      setNodes((nds) =>
        nodes.map((node) => {
          if (node.id === e.target) {
            node.data = {
              ...node.data,
              processed: true,
              updateData: updateData,
              updateFilter: updateFilter,
              edges: edges.concat(e),
              image: "http://localhost:8080/api/media/previews/" + response.data.image_path
            };
          }

          return node;
      }))
    }
    if(nodes[a].type === "outputNode"){
      setNodes((nds) =>
      nodes.map((node) => {
        if (node.id === e.target) {
          node.data = {
            ...node.data,
            image: nodes[b].data.image
          };
        }
        return node;
      })
      );
    }
    if(nodes[b].type === "filterNode"){
      setNodes((nds) =>
      nodes.map((node) => {
        if (node.id === e.source) {
        node.data = {
          ...node.data,
          edges: edges.concat(e),
          updateData: updateData,
          updateFilter: updateFilter
        };
        }
        return node;
      })
      );
    }

    onConnect(e)
  }

  const handleNodeChange = (e) => {
    const updatedNodes = nodes.map((node) => {
      if (node.type === 'filterNode') {
        node.data = {
          ...node.data,
          edges: edges,
          updateData: updateData,
          updateFilter: updateFilter
        };
      }
      return node;
    });
    setNodes(updatedNodes);
    onNodesChange(e);
  }

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
        onNodesChange={handleNodeChange}
        onEdgesChange={onEdgesChange}
        isValidConnection={validate}
        onConnect={handleConnect}
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
        opened={opened} setOpened={setOpened} addNodeProp={addNodeM}/>
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