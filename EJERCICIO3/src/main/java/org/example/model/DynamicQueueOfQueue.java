package org.example.model;

import org.example.util.QueueUtil;

public class DynamicQueueOfQueue implements QueueOfQueue {

    private NodeQQ first;


    private static class NodeQQ {
        private Queue value;
        private NodeQQ next;

        public NodeQQ(Queue value, NodeQQ next) {
            this.value = value;
            this.next = next;
        }

        public Queue getValue() {
            return value;
        }

        public NodeQQ getNext() {
            return next;
        }

        public void setNext(NodeQQ next) {
            this.next = next;
        }
    }



    @Override
    public boolean isEmpty() {
        return first == null;
    }

    @Override
    public void add(Queue q) {
        if (isEmpty()) {
            first = new NodeQQ(q, null);
        } else {
            NodeQQ current = first;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(new NodeQQ(q, null));
        }
    }

    @Override
    public void remove() {
        if (isEmpty()) {
            throw new RuntimeException("No se puede remover de una QueueOfQueue vacía.");
        }
        first = first.getNext();
    }

    @Override
    public Queue getFirst() {
        if (isEmpty()) {
            throw new RuntimeException("No se puede obtener el primero de una QueueOfQueue vacía.");
        }
        return first.getValue();
    }


    @Override
    public QueueOfQueue concatenate(QueueOfQueue... others) {
        DynamicQueueOfQueue result = new DynamicQueueOfQueue();


        NodeQQ current = first;
        while (current != null) {
            result.add(QueueUtil.copy(current.getValue()));
            current = current.getNext();
        }


        for (QueueOfQueue qoq : others) {
            if (qoq instanceof DynamicQueueOfQueue) {
                DynamicQueueOfQueue dyn = (DynamicQueueOfQueue) qoq;
                NodeQQ otherCurrent = dyn.first;
                while (otherCurrent != null) {
                    result.add(QueueUtil.copy(otherCurrent.getValue()));
                    otherCurrent = otherCurrent.getNext();
                }
            } else {
                throw new RuntimeException("QueueOfQueue no soportada en concatenate");
            }
        }
        return result;
    }

    @Override
    public Queue flat() {
        Queue result = new DynamicQueue();
        NodeQQ current = first;
        while (current != null) {
            Queue copyQ = QueueUtil.copy(current.getValue());
            while (!copyQ.isEmpty()) {
                result.add(copyQ.getFirst());
                copyQ.remove();
            }
            current = current.getNext();
        }
        return result;
    }

    @Override
    public void reverseWithDepth() {
        reverseWithDepthRecursive();
    }

    private void reverseWithDepthRecursive() {
        if (isEmpty()) {
            return;
        }
        Queue front = getFirst();
        remove();
        reverseWithDepthRecursive();
        reverseQueue(front);
        add(front);
    }

    private void reverseQueue(Queue q) {
        if (q.isEmpty()) {
            return;
        }
        int firstElem = q.getFirst();
        q.remove();
        reverseQueue(q);
        q.add(firstElem);
    }
}
