import React, { useCallback } from 'react';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
} from 'reactflow';

import ImageNode from '../Editor/ImageNode'
import 'reactflow/dist/style.css';
 
const initialNodes = [
  { id: '1', position: { x: 150, y: 100 }, data: { label: '1' } },
  { id: '2', position: { x: 150, y: 200 }, data: { label: '2' } },
  { id: '3', position: { x: 250, y: 300 }, data: { name: "Papich", label: '2', image: "http://localhost:8080/api/videos/previews/video_20240419_132126_1295.png" }, type: 'imageNode'}
];

const nodeTypes = {
    imageNode: ImageNode,
  };

const initialEdges = [{ id: 'e1-2', source: '1', target: '2' }];
 
export default function App() {
  // eslint-disable-next-line
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
 
  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges],
  );
 
  return (
    <div style={{ width: '100vw', height: '100vh' }}>
      <ReactFlow
        nodes={nodes}
        edges={edges}
        nodeTypes={nodeTypes}
        onNodesChange={onNodesChange}
        onEdgesChange={onEdgesChange}
        onConnect={onConnect}
      >
        <Controls />
        <MiniMap />
        <Background variant="dots" gap={12} size={1} />
      </ReactFlow>
    </div>
  );
}