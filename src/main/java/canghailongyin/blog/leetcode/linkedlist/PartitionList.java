package canghailongyin.blog.leetcode.linkedlist;

import canghailongyin.blog.basic_structure.ListNode;

/**
 * Created by mingl on 2017-9-15.
 */
public class PartitionList {
    public ListNode partition(ListNode head , int val){
        if(head == null)
            return head;
        ListNode bigHead = new ListNode(-1);
        ListNode smallHead = new ListNode(-1);
        ListNode bigHeadNode = bigHead;
        ListNode smallHeadNode = smallHead;
        while(head != null){
            int curVal = head.val;
            if(curVal >= val){
                bigHead.next = head;
                bigHead = bigHead.next;
            }else{
                smallHead.next = head;
                smallHead = smallHead.next;
            }
            head = head.next;
        }
        //把小头的next指向大头的开始
        smallHead.next = bigHeadNode;
        bigHead.next = null;
        return smallHeadNode;
    }
}
