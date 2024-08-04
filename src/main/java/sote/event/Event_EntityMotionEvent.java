package sote.event;

import cn.nukkit.event.entity.EntityMotionEvent;

public class Event_EntityMotionEvent{

    public Event_EntityMotionEvent(){
    }

    public static void onMove(EntityMotionEvent event){
        /*Entity entityy = event.getEntity();
        Vector3 motion = event.getMotion();
        if(entityy instanceof EntityArrow){
            EntityArrow entity = (EntityArrow) entityy;
            MovingObjectPosition movingObjectPosition = null;
            Vector3 moveVector = new Vector3(entity.x,entity.y,entity.z);
            Entity[] list = entity.getLevel().getCollidingEntities(entity.boundingBox.addCoord(motion.x, motion.y, motion.z).expand(1, 1, 1), entity);
            double nearDistance = Integer.MAX_VALUE;
            Entity nearEntity = null;
            for (Entity entities : list) {
                if ((entity == entity.shootingEntity && entity.ticksLived < 5)) {
                    continue;
                }
                AxisAlignedBB axisalignedbb = entities.boundingBox.grow(1, 1, 1);
                MovingObjectPosition ob = axisalignedbb.calculateIntercept(entity, moveVector);
                if (ob == null) {
                    continue;
                }
                double distance = entity.distanceSquared(ob.hitVector);
                if (distance < nearDistance) {
                    nearDistance = distance;
                    nearEntity = entities;
                }
                EntityDamageEvent ev;
                if (movingObjectPosition != null && movingObjectPosition.entityHit != null){
                    ev = new EntityDamageByChildEntityEvent(entity.shootingEntity, entity, movingObjectPosition.entityHit, EntityDamageEvent.CAUSE_PROJECTILE, (float) 2);
                    Server.getInstance().getPluginManager().callEvent(ev);
                    entity.kill();
                }
            }
            if (nearEntity != null) {
                movingObjectPosition = MovingObjectPosition.fromEntity(nearEntity);
            }
        }*/
    }
}