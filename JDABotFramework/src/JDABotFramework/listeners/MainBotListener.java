package JDABotFramework.listeners;

import java.util.HashMap;

import JDABotFramework.global.config.BotGlobalConfig;
import JDABotFramework.react.ReactionController;
import JDABotFramework.util.command.CmdControl;
import net.dv8tion.jda.core.events.DisconnectEvent;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.ExceptionEvent;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.ReconnectedEvent;
import net.dv8tion.jda.core.events.ResumedEvent;
import net.dv8tion.jda.core.events.ShutdownEvent;
import net.dv8tion.jda.core.events.StatusChangeEvent;
import net.dv8tion.jda.core.events.channel.priv.PrivateChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.priv.PrivateChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateNSFWEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdatePositionEvent;
import net.dv8tion.jda.core.events.channel.text.update.TextChannelUpdateTopicEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.core.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdateBitrateEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdateNameEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdatePositionEvent;
import net.dv8tion.jda.core.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;
import net.dv8tion.jda.core.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.core.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.core.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.core.events.guild.GuildAvailableEvent;
import net.dv8tion.jda.core.events.guild.GuildBanEvent;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.core.events.guild.GuildUnavailableEvent;
import net.dv8tion.jda.core.events.guild.GuildUnbanEvent;
import net.dv8tion.jda.core.events.guild.UnavailableGuildJoinedEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberNickChangeEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateAfkChannelEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateAfkTimeoutEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateMFALevelEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateNotificationLevelEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateOwnerEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateRegionEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateSplashEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateSystemChannelEvent;
import net.dv8tion.jda.core.events.guild.update.GuildUpdateVerificationLevelEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceMuteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceSelfDeafenEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceSelfMuteEvent;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceSuppressEvent;
import net.dv8tion.jda.core.events.http.HttpRequestEvent;
import net.dv8tion.jda.core.events.message.MessageBulkDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageDeleteEvent;
import net.dv8tion.jda.core.events.message.MessageEmbedEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.core.events.role.RoleCreateEvent;
import net.dv8tion.jda.core.events.role.RoleDeleteEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateHoistedEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateMentionableEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdatePermissionsEvent;
import net.dv8tion.jda.core.events.role.update.RoleUpdatePositionEvent;
import net.dv8tion.jda.core.events.self.SelfUpdateAvatarEvent;
import net.dv8tion.jda.core.events.self.SelfUpdateEmailEvent;
import net.dv8tion.jda.core.events.self.SelfUpdateMFAEvent;
import net.dv8tion.jda.core.events.self.SelfUpdateNameEvent;
import net.dv8tion.jda.core.events.self.SelfUpdateVerifiedEvent;
import net.dv8tion.jda.core.events.user.UserTypingEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateGameEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.core.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * A modular based bot listener allows for chaining multiple events stuffs, similar to bash
 * using java's lambda expressions.
 * @author Allen
 *
 */
public class MainBotListener extends ListenerAdapter {
	private HashMap<Class<? extends Event>,EventListenerChain<?>> chains = new HashMap<Class<? extends Event>,EventListenerChain<?>>();
	
	private final BotGlobalConfig config;
	
	/**
	 * Populate chains map with all the ListenerChains
	 */
	private void buildChains(){
		//JDA events
		chains.put(ReadyEvent.class,new EventListenerChain<ReadyEvent>(ReadyEvent.class));
		chains.put(ResumedEvent.class, new EventListenerChain<ResumedEvent>(ResumedEvent.class));
		chains.put(ReconnectedEvent.class, new EventListenerChain<ReconnectedEvent>(ReconnectedEvent.class));
		chains.put(DisconnectEvent.class, new EventListenerChain<DisconnectEvent>(DisconnectEvent.class));
		chains.put(ShutdownEvent.class, new EventListenerChain<ShutdownEvent>(ShutdownEvent.class));
		chains.put(StatusChangeEvent.class, new EventListenerChain<StatusChangeEvent>(StatusChangeEvent.class));
		chains.put(ExceptionEvent.class, new EventListenerChain<ExceptionEvent>(ExceptionEvent.class));
		
		//User events
		chains.put(UserUpdateNameEvent.class, new EventListenerChain<UserUpdateNameEvent>(UserUpdateNameEvent.class));
		chains.put(UserUpdateAvatarEvent.class, new EventListenerChain<UserUpdateAvatarEvent>(UserUpdateAvatarEvent.class));
		chains.put(UserUpdateOnlineStatusEvent.class, new EventListenerChain<UserUpdateOnlineStatusEvent>(UserUpdateOnlineStatusEvent.class));
		chains.put(UserUpdateGameEvent.class, new EventListenerChain<UserUpdateGameEvent>(UserUpdateGameEvent.class));
		chains.put(UserTypingEvent.class, new EventListenerChain<UserTypingEvent>(UserTypingEvent.class));
		
		//self events
		chains.put(SelfUpdateAvatarEvent.class, new EventListenerChain<SelfUpdateAvatarEvent>(SelfUpdateAvatarEvent.class));
		chains.put(SelfUpdateEmailEvent.class,new EventListenerChain<SelfUpdateEmailEvent>(SelfUpdateEmailEvent.class));
		chains.put(SelfUpdateMFAEvent.class, new EventListenerChain<SelfUpdateMFAEvent>(SelfUpdateMFAEvent.class));
		chains.put(SelfUpdateNameEvent.class, new EventListenerChain<SelfUpdateNameEvent>(SelfUpdateNameEvent.class));
		chains.put(SelfUpdateVerifiedEvent.class, new EventListenerChain<SelfUpdateVerifiedEvent>(SelfUpdateVerifiedEvent.class));
		
		//skipping individual stuff for now
		//combined MessageEvents
		chains.put(MessageReceivedEvent.class,new EventListenerChain<MessageReceivedEvent>(MessageReceivedEvent.class));
		chains.put(MessageUpdateEvent.class, new EventListenerChain<MessageUpdateEvent>(MessageUpdateEvent.class));
		chains.put(MessageDeleteEvent.class, new EventListenerChain<MessageDeleteEvent>(MessageDeleteEvent.class));
		chains.put(MessageBulkDeleteEvent.class, new EventListenerChain<MessageBulkDeleteEvent>(MessageBulkDeleteEvent.class));
		chains.put(MessageEmbedEvent.class, new EventListenerChain<MessageEmbedEvent>(MessageEmbedEvent.class));
		chains.put(MessageReactionAddEvent.class,new EventListenerChain<MessageReactionAddEvent>(MessageReactionAddEvent.class));
		chains.put(MessageReactionRemoveEvent.class,new EventListenerChain<MessageReactionRemoveEvent>(MessageReactionRemoveEvent.class));
		chains.put(MessageReactionRemoveAllEvent.class, new EventListenerChain<MessageReactionRemoveAllEvent>(MessageReactionRemoveAllEvent.class));
		
		//TextChannel
		chains.put(TextChannelDeleteEvent.class, new EventListenerChain<TextChannelDeleteEvent>(TextChannelDeleteEvent.class));
		chains.put(TextChannelUpdateNameEvent.class, new EventListenerChain<TextChannelUpdateNameEvent>(TextChannelUpdateNameEvent.class));
		chains.put(TextChannelUpdateTopicEvent.class, new EventListenerChain<TextChannelUpdateTopicEvent>(TextChannelUpdateTopicEvent.class));
		chains.put(TextChannelUpdatePositionEvent.class, new EventListenerChain<TextChannelUpdatePositionEvent>(TextChannelUpdatePositionEvent.class));
		chains.put(TextChannelUpdatePermissionsEvent.class, new EventListenerChain<TextChannelUpdatePermissionsEvent>(TextChannelUpdatePermissionsEvent.class));
		chains.put(TextChannelUpdateNSFWEvent.class, new EventListenerChain<TextChannelUpdateNSFWEvent>(TextChannelUpdateNSFWEvent.class));
		chains.put(TextChannelCreateEvent.class, new EventListenerChain<TextChannelCreateEvent>(TextChannelCreateEvent.class));
		
		//Voice Channel
		chains.put(VoiceChannelDeleteEvent.class, new EventListenerChain<VoiceChannelDeleteEvent>(VoiceChannelDeleteEvent.class));
		chains.put(VoiceChannelUpdateNameEvent.class, new EventListenerChain<VoiceChannelUpdateNameEvent>(VoiceChannelUpdateNameEvent.class));
		chains.put(VoiceChannelUpdatePositionEvent.class, new EventListenerChain<VoiceChannelUpdatePositionEvent>(VoiceChannelUpdatePositionEvent.class));
		chains.put(VoiceChannelUpdateUserLimitEvent.class, new EventListenerChain<VoiceChannelUpdateUserLimitEvent>(VoiceChannelUpdateUserLimitEvent.class));
		chains.put(VoiceChannelUpdateBitrateEvent.class, new EventListenerChain<VoiceChannelUpdateBitrateEvent>(VoiceChannelUpdateBitrateEvent.class));
		chains.put(VoiceChannelUpdatePermissionsEvent.class, new EventListenerChain<VoiceChannelUpdatePermissionsEvent>(VoiceChannelUpdatePermissionsEvent.class));
		chains.put(VoiceChannelCreateEvent.class, new EventListenerChain<VoiceChannelCreateEvent>(VoiceChannelCreateEvent.class));
		
		//PrivateChannel
		chains.put(PrivateChannelCreateEvent.class, new EventListenerChain<PrivateChannelCreateEvent>(PrivateChannelCreateEvent.class));
		chains.put(PrivateChannelDeleteEvent.class, new EventListenerChain<PrivateChannelDeleteEvent>(PrivateChannelDeleteEvent.class));
		
		//Guild
		chains.put(GuildJoinEvent.class, new EventListenerChain<GuildJoinEvent>(GuildJoinEvent.class));
		chains.put(GuildLeaveEvent.class, new EventListenerChain<GuildLeaveEvent>(GuildLeaveEvent.class));
		chains.put(GuildAvailableEvent.class, new EventListenerChain<GuildAvailableEvent>(GuildAvailableEvent.class));
		chains.put(GuildUnavailableEvent.class, new EventListenerChain<GuildUnavailableEvent>(GuildUnavailableEvent.class));
		chains.put(GuildBanEvent.class, new EventListenerChain<GuildBanEvent>(GuildBanEvent.class));
		chains.put(GuildUnbanEvent.class,new EventListenerChain<GuildUnbanEvent>(GuildUnbanEvent.class));
		
		//Guild Update
		chains.put(GuildUpdateAfkChannelEvent.class, new EventListenerChain<GuildUpdateAfkChannelEvent>(GuildUpdateAfkChannelEvent.class));
		chains.put(GuildUpdateSystemChannelEvent.class, new EventListenerChain<GuildUpdateSystemChannelEvent>(GuildUpdateSystemChannelEvent.class));
		chains.put(GuildUpdateAfkTimeoutEvent.class, new EventListenerChain<GuildUpdateAfkTimeoutEvent>(GuildUpdateAfkTimeoutEvent.class));
		chains.put(GuildUpdateIconEvent.class, new EventListenerChain<GuildUpdateIconEvent>(GuildUpdateIconEvent.class));
		chains.put(GuildUpdateMFALevelEvent.class, new EventListenerChain<GuildUpdateMFALevelEvent>(GuildUpdateMFALevelEvent.class));
		chains.put(GuildUpdateNameEvent.class, new EventListenerChain<GuildUpdateNameEvent>(GuildUpdateNameEvent.class));
		chains.put(GuildUpdateNotificationLevelEvent.class, new EventListenerChain<GuildUpdateNotificationLevelEvent>(GuildUpdateNotificationLevelEvent.class));
		chains.put(GuildUpdateOwnerEvent.class, new EventListenerChain<GuildUpdateOwnerEvent>(GuildUpdateOwnerEvent.class));
		chains.put(GuildUpdateRegionEvent.class, new EventListenerChain<GuildUpdateRegionEvent>(GuildUpdateRegionEvent.class));
		chains.put(GuildUpdateSplashEvent.class, new EventListenerChain<GuildUpdateSplashEvent>(GuildUpdateSplashEvent.class));
		chains.put(GuildUpdateVerificationLevelEvent.class, new EventListenerChain<GuildUpdateVerificationLevelEvent>(GuildUpdateVerificationLevelEvent.class));
		
		//Guild Member
		chains.put(GuildMemberJoinEvent.class, new EventListenerChain<GuildMemberJoinEvent>(GuildMemberJoinEvent.class));
		chains.put(GuildMemberLeaveEvent.class, new EventListenerChain<GuildMemberLeaveEvent>(GuildMemberLeaveEvent.class));
		chains.put(GuildMemberRoleAddEvent.class, new EventListenerChain<GuildMemberRoleAddEvent>(GuildMemberRoleAddEvent.class));
		chains.put(GuildMemberRoleRemoveEvent.class, new EventListenerChain<GuildMemberRoleRemoveEvent>(GuildMemberRoleRemoveEvent.class));
		chains.put(GuildMemberNickChangeEvent.class, new EventListenerChain<GuildMemberNickChangeEvent>(GuildMemberNickChangeEvent.class));
		
		//Guild Voice
		chains.put(GuildVoiceJoinEvent.class, new EventListenerChain<GuildVoiceJoinEvent>(GuildVoiceJoinEvent.class));
		chains.put(GuildVoiceMoveEvent.class, new EventListenerChain<GuildVoiceMoveEvent>(GuildVoiceMoveEvent.class));
		chains.put(GuildVoiceLeaveEvent.class, new EventListenerChain<GuildVoiceLeaveEvent>(GuildVoiceLeaveEvent.class));
		chains.put(GuildVoiceMuteEvent.class, new EventListenerChain<GuildVoiceMuteEvent>(GuildVoiceMuteEvent.class));
		chains.put(GuildVoiceDeafenEvent.class, new EventListenerChain<GuildVoiceDeafenEvent>(GuildVoiceDeafenEvent.class));
		chains.put(GuildVoiceGuildMuteEvent.class, new EventListenerChain<GuildVoiceGuildMuteEvent>(GuildVoiceGuildMuteEvent.class));
		chains.put(GuildVoiceGuildDeafenEvent.class, new EventListenerChain<GuildVoiceGuildDeafenEvent>(GuildVoiceGuildDeafenEvent.class));
		chains.put(GuildVoiceSelfMuteEvent.class, new EventListenerChain<GuildVoiceSelfMuteEvent>(GuildVoiceSelfMuteEvent.class));
		chains.put(GuildVoiceSelfDeafenEvent.class, new EventListenerChain<GuildVoiceSelfDeafenEvent>(GuildVoiceSelfDeafenEvent.class));
		chains.put(GuildVoiceSuppressEvent.class, new EventListenerChain<GuildVoiceSuppressEvent>(GuildVoiceSuppressEvent.class));
		
		//Role
		chains.put(RoleCreateEvent.class, new EventListenerChain<RoleCreateEvent>(RoleCreateEvent.class));
		chains.put(RoleDeleteEvent.class, new EventListenerChain<RoleDeleteEvent>(RoleDeleteEvent.class));
		
		//Role Update
		chains.put(RoleUpdateColorEvent.class, new EventListenerChain<RoleUpdateColorEvent>(RoleUpdateColorEvent.class));
		chains.put(RoleUpdateHoistedEvent.class, new EventListenerChain<RoleUpdateHoistedEvent>(RoleUpdateHoistedEvent.class));
		chains.put(RoleUpdateMentionableEvent.class, new EventListenerChain<RoleUpdateMentionableEvent>(RoleUpdateMentionableEvent.class));
		chains.put(RoleUpdateNameEvent.class, new EventListenerChain<RoleUpdateNameEvent>(RoleUpdateNameEvent.class));
		chains.put(RoleUpdatePermissionsEvent.class, new EventListenerChain<RoleUpdatePermissionsEvent>(RoleUpdatePermissionsEvent.class));
		chains.put(RoleUpdatePositionEvent.class, new EventListenerChain<RoleUpdatePositionEvent>(RoleUpdatePositionEvent.class));
		
		//Emote
		chains.put(EmoteAddedEvent.class, new EventListenerChain<EmoteAddedEvent>(EmoteAddedEvent.class));
		chains.put(EmoteRemovedEvent.class, new EventListenerChain<EmoteRemovedEvent>(EmoteRemovedEvent.class));
		
		//Emote Update
		chains.put(EmoteUpdateNameEvent.class, new EventListenerChain<EmoteUpdateNameEvent>(EmoteUpdateNameEvent.class));
		chains.put(EmoteUpdateRolesEvent.class, new EventListenerChain<EmoteUpdateRolesEvent>(EmoteUpdateRolesEvent.class));
		
		//Debug
		chains.put(HttpRequestEvent.class, new EventListenerChain<HttpRequestEvent>(HttpRequestEvent.class));
	}
	
	public MainBotListener(CmdControl cmd,ReactionController react,BotGlobalConfig config){
		this.config=config;
		//add all the chains stuffs
		buildChains();
		//add soem basic listeners, for command controller, reaction controller
		getEventChain(MessageReceivedEvent.class)
			.addListener("Command Controller",(e,con) -> {
			return cmd.parseCommands(e);
		});
		getEventChain(MessageReceivedEvent.class)
			.addListener("Prefix Listener", new PrefixListener());
		getEventChain(MessageReactionAddEvent.class)
			.addListener("Reaction Controller", (e,con)->{
			return react.parseReaction(e);
		});
		getEventChain(MessageReactionRemoveEvent.class)
			.addListener("ReactionController", (e,con)->{
				return react.parseReaction(e);
		});
	}
	
	/**
	 * Gets the ListenerChain for the type, does appropriate casts
	 * @param type event that the chain listens for
	 * @return EventListenerChain of type <type>
	 */
	@SuppressWarnings("unchecked")
	public <T extends Event> EventListenerChain<T> getEventChain(Class<T> type){
		return (EventListenerChain<T>) chains.get(type);
	}
	
	//overrides for listeners in the ListenerAdapter
	@Override
	public void onReady(ReadyEvent event){
		getEventChain(ReadyEvent.class).executeChain(event, config);
	}
	@Override
	public void onResume(ResumedEvent event){
		getEventChain(ResumedEvent.class).executeChain(event, config);
	}
	@Override
	public void onReconnect(ReconnectedEvent event){
		getEventChain(ReconnectedEvent.class).executeChain(event, config);
	}
	@Override
	public void onDisconnect(DisconnectEvent event){
		getEventChain(DisconnectEvent.class).executeChain(event, config);
	}
	@Override
	public void onShutdown(ShutdownEvent event){
		getEventChain(ShutdownEvent.class).executeChain(event, config);
	}
	@Override
	public void onStatusChange(StatusChangeEvent event){
		getEventChain(StatusChangeEvent.class).executeChain(event, config);
	}
	@Override
	public void onException(ExceptionEvent event){
		getEventChain(ExceptionEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event){
		getEventChain(MessageReceivedEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageUpdate(MessageUpdateEvent event){
		getEventChain(MessageUpdateEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageDelete(MessageDeleteEvent event){
		getEventChain(MessageDeleteEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageEmbed(MessageEmbedEvent event){
		getEventChain(MessageEmbedEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event) {
		getEventChain(MessageReactionAddEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent event){
		getEventChain(MessageReactionRemoveEvent.class).executeChain(event, config);
	}
	@Override
	public void onMessageReactionRemoveAll(MessageReactionRemoveAllEvent event){
		getEventChain(MessageReactionRemoveAllEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelDelete(TextChannelDeleteEvent event){
		getEventChain(TextChannelDeleteEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelUpdateName(TextChannelUpdateNameEvent event){
		getEventChain(TextChannelUpdateNameEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelUpdateTopic(TextChannelUpdateTopicEvent event){
		getEventChain(TextChannelUpdateTopicEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelUpdatePosition(TextChannelUpdatePositionEvent event){
		getEventChain(TextChannelUpdatePositionEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelUpdatePermissions(TextChannelUpdatePermissionsEvent event){
		getEventChain(TextChannelUpdatePermissionsEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelUpdateNSFW(TextChannelUpdateNSFWEvent event){
		getEventChain(TextChannelUpdateNSFWEvent.class).executeChain(event, config);
	}
	@Override
	public void onTextChannelCreate(TextChannelCreateEvent event){
		getEventChain(TextChannelCreateEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelDelete(VoiceChannelDeleteEvent event){
		getEventChain(VoiceChannelDeleteEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelUpdateName(VoiceChannelUpdateNameEvent event){
		getEventChain(VoiceChannelUpdateNameEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelUpdatePosition(VoiceChannelUpdatePositionEvent event){
		getEventChain(VoiceChannelUpdatePositionEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelUpdateUserLimit(VoiceChannelUpdateUserLimitEvent event){
		getEventChain(VoiceChannelUpdateUserLimitEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelUpdateBitrate(VoiceChannelUpdateBitrateEvent event){
		getEventChain(VoiceChannelUpdateBitrateEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelUpdatePermissions(VoiceChannelUpdatePermissionsEvent event){
		getEventChain(VoiceChannelUpdatePermissionsEvent.class).executeChain(event, config);
	}
	@Override
	public void onVoiceChannelCreate(VoiceChannelCreateEvent event){
		getEventChain(VoiceChannelCreateEvent.class).executeChain(event, config);
	}
	@Override
	public void onPrivateChannelCreate(PrivateChannelCreateEvent event){
		getEventChain(PrivateChannelCreateEvent.class).executeChain(event, config);
	}
	@Override
	public void onPrivateChannelDelete(PrivateChannelDeleteEvent event){
		getEventChain(PrivateChannelDeleteEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildJoin(GuildJoinEvent event){
		getEventChain(GuildJoinEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildLeave(GuildLeaveEvent event){
		getEventChain(GuildLeaveEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildAvailable(GuildAvailableEvent event){
		getEventChain(GuildAvailableEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUnavailable(GuildUnavailableEvent event){
		getEventChain(GuildUnavailableEvent.class).executeChain(event, config);
	}
	@Override
	public void onUnavailableGuildJoined(UnavailableGuildJoinedEvent event){
		getEventChain(UnavailableGuildJoinedEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildBan(GuildBanEvent event){
		getEventChain(GuildBanEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUnban(GuildUnbanEvent event){
		getEventChain(GuildUnbanEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateAfkChannel(GuildUpdateAfkChannelEvent event){
		getEventChain(GuildUpdateAfkChannelEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateSystemChannel(GuildUpdateSystemChannelEvent event){
		getEventChain(GuildUpdateSystemChannelEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateAfkTimeout(GuildUpdateAfkTimeoutEvent event){
		getEventChain(GuildUpdateAfkTimeoutEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateIcon(GuildUpdateIconEvent event){
		getEventChain(GuildUpdateIconEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateMFALevel(GuildUpdateMFALevelEvent event){
		getEventChain(GuildUpdateMFALevelEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateName(GuildUpdateNameEvent event){
		getEventChain(GuildUpdateNameEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateNotificationLevel(GuildUpdateNotificationLevelEvent event){
		getEventChain(GuildUpdateNotificationLevelEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateOwner(GuildUpdateOwnerEvent event){
		getEventChain(GuildUpdateOwnerEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateRegion(GuildUpdateRegionEvent event){
		getEventChain(GuildUpdateRegionEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateSplash(GuildUpdateSplashEvent event){
		getEventChain(GuildUpdateSplashEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildUpdateVerificationLevel(GuildUpdateVerificationLevelEvent event){
		getEventChain(GuildUpdateVerificationLevelEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event){
		getEventChain(GuildMemberJoinEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event){
		getEventChain(GuildMemberLeaveEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event){
		getEventChain(GuildMemberRoleAddEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event){
		getEventChain(GuildMemberRoleRemoveEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildMemberNickChange(GuildMemberNickChangeEvent event){
		getEventChain(GuildMemberNickChangeEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
		getEventChain(GuildVoiceJoinEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceMove(GuildVoiceMoveEvent event){
		getEventChain(GuildVoiceMoveEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event){
		getEventChain(GuildVoiceLeaveEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceMute(GuildVoiceMuteEvent event){
		getEventChain(GuildVoiceMuteEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceDeafen(GuildVoiceDeafenEvent event){
		getEventChain(GuildVoiceDeafenEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceGuildMute(GuildVoiceGuildMuteEvent event){
		getEventChain(GuildVoiceGuildMuteEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceGuildDeafen(GuildVoiceGuildDeafenEvent event){
		getEventChain(GuildVoiceGuildDeafenEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceSelfMute(GuildVoiceSelfMuteEvent event){
		getEventChain(GuildVoiceSelfMuteEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceSelfDeafen(GuildVoiceSelfDeafenEvent event){
		getEventChain(GuildVoiceSelfDeafenEvent.class).executeChain(event, config);
	}
	@Override
	public void onGuildVoiceSuppress(GuildVoiceSuppressEvent event){
		getEventChain(GuildVoiceSuppressEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleCreate(RoleCreateEvent event){
		getEventChain(RoleCreateEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleDelete(RoleDeleteEvent event){
		getEventChain(RoleDeleteEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleUpdateColor(RoleUpdateColorEvent event){
		getEventChain(RoleUpdateColorEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleUpdateHoisted(RoleUpdateHoistedEvent event){
		getEventChain(RoleUpdateHoistedEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleUpdateMentionable(RoleUpdateMentionableEvent event){
		getEventChain(RoleUpdateMentionableEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleUpdateName(RoleUpdateNameEvent event){
		getEventChain(RoleUpdateNameEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleUpdatePermissions(RoleUpdatePermissionsEvent event){
		getEventChain(RoleUpdatePermissionsEvent.class).executeChain(event, config);
	}
	@Override
	public void onRoleUpdatePosition(RoleUpdatePositionEvent event){
		getEventChain(RoleUpdatePositionEvent.class).executeChain(event, config);
	}
	@Override
	public void onEmoteAdded(EmoteAddedEvent event){
		getEventChain(EmoteAddedEvent.class).executeChain(event, config);
	}
	@Override
	public void onEmoteRemoved(EmoteRemovedEvent event){
		getEventChain(EmoteRemovedEvent.class).executeChain(event, config);
	}
	@Override
	public void onEmoteUpdateName(EmoteUpdateNameEvent event){
		getEventChain(EmoteUpdateNameEvent.class).executeChain(event, config);
	}
	@Override
	public void onEmoteUpdateRoles(EmoteUpdateRolesEvent event){
		getEventChain(EmoteUpdateRolesEvent.class).executeChain(event, config);
	}
	@Override
	public void onHttpRequest(HttpRequestEvent event){
		getEventChain(HttpRequestEvent.class).executeChain(event, config);
	}
}