package canghailongyin.blog.leetcode.linkedlist;

import canghailongyin.blog.basic_structure.ListNode;

/**
 * Created by mingl on 2017-9-9.
 * 终于不怕单链表翻转了！！
 */
public class ResverseLinkList {

    public ListNode resverseLinkList(ListNode head){
        if(head == null)
            return null;
        ListNode newHead = null;
        ListNode curNode = head;
        ListNode preNode = null;
        while(curNode != null){
            ListNode nextNode = curNode.next;
            if(nextNode == null){
                newHead = curNode;
            }
            curNode.next = preNode;
            preNode = curNode;
            curNode = nextNode;
        }
        return newHead;
    }

    /**
     * Given 1->2->3->4->5->NULL, m = 2 and n = 4,

     return 1->4->3->2->5->NULL.
     * @param head
     * @param start
     * @param end
     * @return
     */
    public ListNode resverseLinkList(ListNode head,int start,int end){
        if(head == null){
            return null;
        }
        int count = 1;
        ListNode newHead = null;
        ListNode curNode = head;
        ListNode preNode = null;
        while(curNode != null){

            if(count == start){//说明达到了要转换的点

            }else{

            }
        }
        return null;

    }


}
